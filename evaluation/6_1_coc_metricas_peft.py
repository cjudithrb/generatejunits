
# -*- coding: utf-8 -*-
"""Metricas.ipynb"""

## **1: Pre-requisitos**

### **Importación de Librerías Optimizada**
import os
import torch
import pandas as pd
import numpy as np
import re
import evaluate
import datetime
import pytz
import wandb
import code_bert_score

from transformers import AutoModelForCausalLM, AutoTokenizer
from datasets import load_dataset, Dataset
from peft import PeftModel, PeftConfig
from huggingface_hub import HfApi, login
from codebleu import calc_codebleu

# Configuración de variables de entorno
os.environ['CUDA_LAUNCH_BLOCKING'] = '1'
os.environ["TOKENIZERS_PARALLELISM"] = "false"
os.environ['PYTORCH_CUDA_ALLOC_CONF'] = 'expandable_segments:True'
os.environ["WANDB_API_KEY"] = "b892949c50481e4e5ecdf7b73dbebdd22d1e5892"
os.environ["HUGGING_FACE_HUB_TOKEN"] = "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"

def init_device():
    """Configura y retorna el dispositivo basado en la disponibilidad de CUDA."""
    device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
    print(f"Usando dispositivo: {device}")
    return device

def load_model_and_tokenizer(base_model_name, is_peft=False, peft_repo=None):
    """Carga un modelo y tokenizador (PEFT opcional), moviéndolos al dispositivo."""
    device = init_device()
    model = AutoModelForCausalLM.from_pretrained(base_model_name, device_map="auto")

    if is_peft and peft_repo:
        config = PeftConfig.from_pretrained(peft_repo)
        model = PeftModel.from_pretrained(model, peft_repo, device_map={"": device})
        tokenizer_name = peft_repo
    else:
        tokenizer_name = base_model_name

    tokenizer = AutoTokenizer.from_pretrained(tokenizer_name, use_fast=True)
    tokenizer.pad_token = tokenizer.eos_token
    print("Modelo y tokenizador cargados.")
    return model, tokenizer

def init_wandb(project_name, model_name, file_name):
    """Inicializa W&B para el seguimiento de experimentos."""
    lima_tz = pytz.timezone('America/Lima')
    current_datetime = datetime.datetime.now(lima_tz).strftime("%Y%m%d_%H%M%S")
    wandb.init(project=project_name, name=f"Eval_{model_name}_{file_name}_{current_datetime}", mode="offline")
    print("W&B inicializado.")

def clean_description(text):
    """Limpia la columna 'description' en el DataFrame."""
    text = re.sub(r"^[^\w\s]+", "/*", text)
    text = re.sub(r"^/\*\n\*\n", "/*\n", text)
    text = re.sub(r"[^\w\s]+$", "*/", text)
    text = re.sub(r"', \*/$", "", text)
    text = re.sub(r"[^\w\s\/\*;@<>{}]", "", text)
    text = re.sub(r"(/\s*,\s*//|\s*,\s*,\s*//|,\s*,\s*,\s*,\s*//)", "", text)
    text = re.sub(r'<p>|<\/p>', '', text)
    text = re.sub(r'\*/ \) \( //', '*', text)
    text = re.sub(r'\) \( //', '*', text)
    return text

def prepare_dataset(file_path):
    """Carga y limpia el dataset, filtrando registros según criterios específicos."""
    df = pd.read_csv(file_path, sep='\t')
    df['description'] = df['description'].apply(clean_description)
#    df['num_characters_description'] = df['description'].str.len()
#    df['num_characters_test'] = df['test_case'].str.len()
#    df = df[(df['num_characters_description'] > 50) & 
#            (df['num_characters_test'] > 50) & 
#            (df['num_characters_test'] <= 2000)]
    df = df[['description', 'focal_method', 'test_case']]
    return Dataset.from_pandas(df)

def tokenize_preprocess(examples, tokenizer, max_length=1024):
    """Tokeniza inputs y targets para el modelo."""
    model_inputs = tokenizer(examples['description'], truncation=True, padding='max_length', max_length=max_length)
    labels = tokenizer(examples['test_case'], truncation=True, padding='max_length', max_length=max_length)
    model_inputs['labels'] = labels['input_ids']
    return model_inputs

def prepare_test_dataset(dataset, tokenizer):
    """Aplica la tokenización al dataset y retorna el conjunto de prueba."""
    tokenizer.pad_token = tokenizer.eos_token
    tokenized_dataset = dataset.map(lambda x: tokenize_preprocess(x, tokenizer), batched=True)
    return {'test': tokenized_dataset}

# Descargar
rouge = evaluate.load('rouge')
bleu = evaluate.load("bleu")
bertscore = evaluate.load('bertscore')
sacrebleu = evaluate.load('sacrebleu')

# Función de evaluación
def evaluate_model(model_peft, tokenizer_peft, tokenized_datasets, device):
    descriptions = tokenized_datasets['test']['description']
    human_baseline_test_case = tokenized_datasets['test']['test_case']

    base_model_test_cases = []
    peft_model_test_cases = []

    model_peft = model_peft.to(device)
    model_peft.eval()  # Aseguramos que el modelo esté en modo evaluación

    for _, description in enumerate(descriptions):
        prompt = f"""
        Ejemplo:
        Método:
            /**
             * Este método calcula el área de un rectángulo.
             * @param width ancho del rectángulo.
             * @param height altura del rectángulo.
             * @return área calculada.
             */

        Genera un caso de prueba JUnit siguiendo estos pasos:
        1. Propósito: Este método calcula el área de un rectángulo multiplicando su ancho por su altura.
        2. Parámetros: width (ancho) y height (altura), ambos deben ser números positivos.
        3. Comportamiento esperado: 
            - Si width = 4 y height = 5, el área debe ser 20.
            - Si width o height son 0, el área debe ser 0.
        4. Test JUnit: \n
            @Test
            public void testCalculateRectangleArea() {{
                assertEquals(20, calculateRectangleArea(4, 5));
                assertEquals(0, calculateRectangleArea(0, 5));
                assertEquals(0, calculateRectangleArea(4, 0));
            }}

        ---

        Nuevo caso:
        Método:{description} \n
        Genera un caso de prueba JUnit siguiendo estos pasos:
        1. Propósito: Describe qué hace este método.
        2. Parámetros: ¿Qué parámetros tendrá el método y qué valores puede tomar?
        3. Comportamiento esperado: Define qué resultado debe devolver este método para diferentes valores de los parámetros.
        4. Test JUnit: \n
        """

        # Tokenizamos y creamos la attention_mask
        inputs = tokenizer_peft(prompt, return_tensors="pt", padding=True, truncation=True)
        input_ids = inputs.input_ids.to(device)
        attention_mask = inputs.attention_mask.to(device)

        # Generación con el modelo PEFT
        with torch.no_grad():
            peft_model_outputs = model_peft.generate(
                input_ids=input_ids,
                attention_mask=attention_mask,
                max_new_tokens=1024,
                pad_token_id=tokenizer_peft.eos_token_id,
                temperature=0.7,
                do_sample=True,
                top_p=0.9,
                num_return_sequences=1
            )

        peft_model_text_output = tokenizer_peft.decode(peft_model_outputs[0], skip_special_tokens=True)
        peft_model_test_cases.append(peft_model_text_output)

        # Limpiamos la caché de CUDA después de cada iteración
        if torch.cuda.is_available():
            torch.cuda.empty_cache()

    # Verificar que las listas tengan la misma longitud
    if len(human_baseline_test_case) == len(peft_model_test_cases):
        zipped_summaries = list(zip(human_baseline_test_case,  peft_model_test_cases))
        df_report = pd.DataFrame(zipped_summaries, columns=['HUMAN_BASELINE_TEST_CASES',  f'PEFT_MODEL_TEST_CASES'])
    else:
        raise ValueError("Las listas de casos de prueba no tienen la misma longitud.")

    print(df_report.head(10))
    print(df_report.iloc[2, 1])

    # Evaluación de métricas
    # 1. Evaluación con Rouge
    peft_model_results_rouge = rouge.compute(
        predictions=peft_model_test_cases,
        references=human_baseline_test_case[0:len(peft_model_test_cases)],
        use_aggregator=True,
        use_stemmer=True,
    )

    # 2. Evaluación con BLEU
    peft_model_results_bleu = bleu.compute(
        predictions=peft_model_test_cases,
        references=[[ref] for ref in human_baseline_test_case[0:len(peft_model_test_cases)]]
    )

    # 3. Evaluación con BERTScore
    peft_model_results_bertscore = bertscore.compute(
        predictions=peft_model_test_cases,
        references=human_baseline_test_case[0:len(peft_model_test_cases)],
        lang="en"
    )
    peft_model_bertscore_precision_avg = np.mean(peft_model_results_bertscore['precision'])
    peft_model_bertscore_recall_avg = np.mean(peft_model_results_bertscore['recall'])
    peft_model_bertscore_f1_avg = np.mean(peft_model_results_bertscore['f1'])

    # 4. Evaluación con SacreBLEU
    peft_model_results_sacrebleu = sacrebleu.compute(
        predictions=peft_model_test_cases,
        references=[[ref] for ref in human_baseline_test_case[0:len(peft_model_test_cases)]]
    )

    # 5. Evaluación con CodeBLEU
    peft_model_results_codebleu = calc_codebleu(
        references=[[ref] for ref in human_baseline_test_case[0:len(peft_model_test_cases)]],
        predictions=peft_model_test_cases,
        lang="java"
    )

    # 6. Evaluación con CodeBERTScore
    codebertscore_peft = code_bert_score.score(
        cands=peft_model_test_cases,
        refs=human_baseline_test_case,
        model_type="./codebert_local",
        lang="java"
    )

    precision_peft, recall_peft, f1_peft, f3_peft = codebertscore_peft
    peft_codebertscore_f1_avg = f1_peft.mean().item()
    peft_codebertscore_f3_avg = f3_peft.mean().item()
    peft_codebertscore_precision_avg = precision_peft.mean().item()
    peft_codebertscore_recall_avg = recall_peft.mean().item()

    # Mostrar resultados de evaluación
    print('\nROUGE:', peft_model_results_rouge)
    print('\nBLEU SCORE:', peft_model_results_bleu)
    print("\nBERTScore Precision Avg:", peft_model_bertscore_precision_avg)
    print("BERTScore Recall Avg:", peft_model_bertscore_recall_avg)
    print("BERTScore F1 Avg:", peft_model_bertscore_f1_avg)
    print("\nSacreBLEU:", peft_model_results_sacrebleu)
    print("\nCodeBLEU:", peft_model_results_codebleu)
    print("\nCodeBERTScore F1:", peft_codebertscore_f1_avg)
    print("CodeBERTScore F3:", peft_codebertscore_f3_avg)
    print("CodeBERTScore Precision:", peft_codebertscore_precision_avg)
    print("CodeBERTScore Recall:", peft_codebertscore_recall_avg)

    # Registrar los resultados de ROUGE en WandB
    wandb.log({
        "Otros/Rouge": peft_model_results_rouge,
        "Test/BLEU": peft_model_results_bleu['bleu'],
        "Otros/BERTScore_Precision": peft_model_bertscore_precision_avg,
        "Otros/BERTScore_Recall": peft_model_bertscore_recall_avg,
        "Test/BERTScore_F1": peft_model_bertscore_f1_avg,
        "Test/SacreBLEU": peft_model_results_sacrebleu['score'],
        "Test/CodeBLEU": peft_model_results_codebleu['codebleu'],
        "Test/CodeBERTScore_F1": peft_codebertscore_f1_avg,
        "Test/CodeBERTScore_F3": peft_codebertscore_f3_avg,
        "Otros/CodeBERTScore_Precision": peft_codebertscore_precision_avg,
        "Otros/CodeBERTScore_Recall": peft_codebertscore_recall_avg,
    })

    # Finalizar sesión de WandB
    wandb.finish()
    torch.cuda.empty_cache()

def main():
    """Punto de entrada principal para cargar, preparar y evaluar el modelo."""
    BASE_MODEL_NAME = "google/gemma-2-2b" #"meta-llama/CodeLlama-7b-hf" #"google/gemma-2-2b" #"Qwen/Qwen2.5-3B" #meta-llama/Llama-3.2-3B 
    MODEL_NAME = BASE_MODEL_NAME.split('/')[-1]
    REPO_HF_NAME = "Judiht/Qwen2_5-3B_d234k_20241109"  #"Judiht/Llama-3_2-3B_d245k_20241108"
    HF_NAME = REPO_HF_NAME.split('/')[-1]
    print(BASE_MODEL_NAME)
    FILE_PATH = '/home/cluny.rojas/my_project/generatejunits/data/3_commons-csv.tsv'
    FILE_NAME = os.path.splitext(os.path.basename(FILE_PATH))[0]
    FILE_NAME = FILE_NAME.replace("dataset", "dt")
    print(FILE_NAME)

    model, tokenizer = load_model_and_tokenizer(BASE_MODEL_NAME)
    dataset = prepare_dataset(FILE_PATH)
    tokenized_datasets = prepare_test_dataset(dataset, tokenizer)
    print(f"Tokenized Dataset Sample: {tokenized_datasets}")
    
    init_wandb("EvaluationTestCases_CSV", MODEL_NAME, FILE_NAME+"_CoC")
    device = init_device()
    evaluate_model(model, tokenizer, tokenized_datasets, device)

if __name__ == "__main__":
    main()    
