# -*- coding: utf-8 -*-
"""5.1_fine_tuning_v1.ipynb

Automatically generated by Colab.


## **1: Pre-requisitos**

### **1.1. Instalación de Dependencias**
"""

"""### **1.2: Importación de Librerías**"""

import os
os.environ['CUDA_LAUNCH_BLOCKING'] = '1'
os.environ["TOKENIZERS_PARALLELISM"] = "false"
os.environ['PYTORCH_CUDA_ALLOC_CONF'] = 'expandable_segments:True'
os.environ["WANDB_API_KEY"] = "WANDB_API_KEY"
import torch
torch.cuda.empty_cache()
import evaluate
import subprocess
import getpass # Para autenticarse en HF
import pandas as pd
import numpy as np
import datetime
import pytz # Zona horaria
import wandb
import re
import sys
import glob

from huggingface_hub import HfApi, login
from transformers import AutoModelForCausalLM, AutoTokenizer #, AutoModelForSeq2SeqLM
from transformers import Trainer, TrainingArguments, EarlyStoppingCallback, TrainerCallback, GenerationConfig, AutoConfig
from datasets import load_dataset, Dataset
from peft import get_peft_model, LoraConfig, TaskType
from peft import PeftModel, PeftConfig
from sklearn.model_selection import train_test_split
from accelerate import Accelerator
from torch.utils.data import DataLoader
from codebleu import calc_codebleu
#from transformers import BitsAndBytesConfig

"""## **2: Configuración**

### **2.1. Definicion de variables**
"""

def clean_model_name(name):
    """Limpia el nombre del modelo, eliminando caracteres no permitidos para Hugging Face Hub."""
    # Reemplazar espacios y puntos con guiones bajos
    name = name.replace(" ", "_").replace(".", "_")
    # Eliminar cualquier otro carácter especial no permitido
    name = re.sub(r'[^a-zA-Z0-9_-]', '', name)
    return name

BASE_MODEL_NAME = "meta-llama/Llama-3.2-3B"  # Asegúrate de seleccionar el modelo adecuado #meta-llama/Llama-3.2-3B / Qwen/Qwen2.5-3B / Qwen/Qwen2.5-1.5B / meta-llama/Llama-3.2-1B / microsoft/phi-1
MODEL_NAME = BASE_MODEL_NAME.split('/')[-1]
CLEAN_MODEL_NAME = clean_model_name(MODEL_NAME)
print(MODEL_NAME)
print(CLEAN_MODEL_NAME)

FILE_PATH = '/home/my_project/generatejunits/data/dataset350k.tsv'
FILE_NAME = os.path.splitext(os.path.basename(FILE_PATH))[0]
print(FILE_NAME)

"""### **2.2. Login Hugging Face**"""

def set_huggingface_hub_token():
    """Configura el token para Hugging Face Hub solicitándolo al usuario y verificando su existencia."""
    if "HUGGING_FACE_HUB_TOKEN" in os.environ:
        print("Token ya configurado.")
    else:
        token = "HUGGING_FACE_HUB_TOKEN"
        login(token=token, add_to_git_credential=True)
        if token:  # Verificar que el token no esté vacío
            os.environ["HUGGING_FACE_HUB_TOKEN"] = token
            print("Token configurado exitosamente.")
        else:
            raise ValueError("No se ingresó un token válido.")

# Uso de la función
try:
    set_huggingface_hub_token()
except ValueError as e:
    print(e)

"""### **2.3. Configuración del Modelo**"""

def get_device():
    """Configura y retorna el dispositivo basado en la disponibilidad de CUDA."""
    device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
    print(f"Usando dispositivo: {device}")
    return device

def set_device():
    """Configura y retorna el dispositivo para entrenamiento basado en la disponibilidad de CUDA."""
    if torch.cuda.is_available():
        print("CUDA está disponible. Usando GPU.")
        return torch.device("cuda")
    else:
        print("CUDA no está disponible. Usando CPU.")
        return torch.device("cpu")

def load_model_and_tokenizer(base_model_name):
    """Carga un modelo y un tokenizador, manejando posibles excepciones durante la carga."""
    try:
        model = AutoModelForCausalLM.from_pretrained(base_model_name, device_map="auto")
        model.gradient_checkpointing_enable()
        tokenizer = AutoTokenizer.from_pretrained(base_model_name, use_fast=True, clean_up_tokenization_spaces=True)
        print("Modelo y tokenizador cargados exitosamente.")
        return model, tokenizer
    except Exception as e:
        print(f"Error al cargar el modelo o el tokenizador: {e}")
        return None, None

# Uso de las funciones
#device = set_device()
model_base, tokenizer = load_model_and_tokenizer(BASE_MODEL_NAME)

if torch.cuda.is_available():
    num_gpus = torch.cuda.device_count()
    print(f"Número de GPUs disponibles: {num_gpus}")
    for i in range(num_gpus):
        print(f"GPU {i}: {torch.cuda.get_device_name(i)}")
else:
    print("No hay GPUs disponibles.")

num_cores = os.cpu_count()
print(f"Número de núcleos de CPU: {num_cores}")


"""## **3: Carga y Preparación del Dataset**"""

df = pd.read_csv(FILE_PATH, sep='\t')
df.head()

"""### **3.1. Limpiar Datos**"""

df['description'] = df['description'].str.replace(r'\\+n', '\n', regex=True)

# Función optimizada con prints para depuración
def clean_description(text):
    """Limpia la columna 'description' y muestra el progreso."""

    #print(f"\nTexto original:\n{text}")

    # 1. Reemplaza caracteres especiales al inicio por '/*'
    text = re.sub(r"^[^\w\s]+", "/*", text)

    # 2. Reemplaza secuencia específica al inicio por '/*\n'
    text = re.sub(r"^/\*\n\*\n", "/*\n", text)

    # 3. Reemplaza caracteres especiales al final por '*/'
    text = re.sub(r"[^\w\s]+$", "*/", text)

    # 4. Elimina la secuencia "', */" al final de la cadena
    text = re.sub(r"', \*/$", "", text)

    # 5. Remueve caracteres especiales excepto los permitidos
    text = re.sub(r"[^\w\s\/\*;@<>{}]", "", text)

    # 6. Reemplaza múltiples patrones con slashes y comas por vacío
    text = re.sub(r"(/\s*,\s*//|\s*,\s*,\s*//|,\s*,\s*,\s*,\s*//)", "", text)

    # 7. Remover etiquetas <p> y </p>
    text = re.sub(r'<p>|<\/p>', '', text)  # Remueve <p> y </p>
    #print(f"\nDespués de eliminar etiquetas <p> y </p>:\n{text}")

    return text

# Aplica la función a la columna 'description' para todos los registros
df['description'] = df['description'].apply(clean_description)

"""### **3.2. Quitar Registros**"""

# Calculating the length of each cell in each column
df['num_characters_description'] = df['description'].apply(lambda x: len(x))
df['num_characters_focal'] = df['focal_method'].apply(lambda x: len(x))
df['num_characters_test'] = df['test_case'].apply(lambda x: len(x))

# Eliminar registros con num_characters_description menores a 50
df = df[df['num_characters_description'] > 50]
print(df.shape)

# Eliminar registros con num_characters_test menores a 120
df = df[df['num_characters_test'] > 120]
print(df.shape)

# Eliminar registros con num_characters_test mayor a 3000
df = df[df['num_characters_test'] <= 1800]
print(df.shape)

df = df[['description', 'focal_method', 'test_case']]
print(df.shape)

"""### **3.3. Convertir a dataset**"""

dataset = Dataset.from_pandas(df)
print(dataset)
print("Dataset cargado")

"""## **4: Entrenamiento**

### **4.1. Tokenizacion**
"""
def tokenize_preprocess(examples, tokenizer, max_length=512):
    """Tokeniza los inputs y los targets, y prepara las entradas y etiquetas para el modelo."""
    model_inputs = tokenizer(examples['description'], truncation=True, padding='max_length', max_length=max_length)

    # Tokenizar los targets utilizando el tokenizer como target_tokenizer
    labels = tokenizer(examples['test_case'], truncation=True, padding='max_length', max_length=max_length)
    model_inputs['labels'] = labels['input_ids']

    return model_inputs

# Función para preparar los conjuntos de datos
def prepare_datasets(dataset: Dataset, tokenizer, test_size: float=0.1, validation_size: float=0.998):
    """Aplica la tokenización y divide el dataset en entrenamiento, prueba y validación."""
    # Configurar el token de padding como el token de fin de secuencia
    tokenizer.pad_token = tokenizer.eos_token

    # Aplicar preprocesamiento al dataset
    tokenized_dataset = dataset.map(lambda x: tokenize_preprocess(x, tokenizer), batched=True)

    # Dividir el dataset en 80% train y 20% resto
    train_test_split = tokenized_dataset.train_test_split(test_size=test_size)
    test_validation_split = train_test_split['test'].train_test_split(test_size=validation_size)

    return {
        'train': train_test_split['train'],
        'test': test_validation_split['test'],
        'validation': test_validation_split['train']
    }

# Uso de las funciones
tokenized_datasets = prepare_datasets(dataset, tokenizer)
print(tokenized_datasets)

"""### **4.2. Configuración LoRA**"""

def configure_lora(task_type, r, lora_alpha, lora_dropout):
    """Configura y retorna los ajustes de LoRA."""
    lora_config = LoraConfig(
        task_type=task_type,
        inference_mode=False,  # Modo de entrenamiento
        r=r,
        lora_alpha=lora_alpha,
        lora_dropout=lora_dropout
    )
    return lora_config

def apply_lora_to_model(model_base, lora_config):
    """Aplica LoRA al modelo y lo mueve al dispositivo especificado."""
    try:
        model = get_peft_model(model_base, lora_config)  # Asumiendo que esta función ya está definida
        #model = model.to(device)
        print("Modelo adaptado con LoRA y movido al dispositivo correctamente.")
        return model
    except Exception as e:
        print(f"Error al aplicar LoRA o mover el modelo al dispositivo: {e}")
        return None

def print_trainable_parameters(model):
    """
    Prints the number of trainable parameters in the model.
    """
    trainable_params = 0
    all_param = 0
    for _, param in model.named_parameters():
        all_param += param.numel()
        if param.requires_grad:
            trainable_params += param.numel()
    print(
        f"trainable params: {trainable_params} || all params: {all_param} || trainable%: {100 * trainable_params / all_param} %"
    )

"""### **4.3. Configuración Finetuning PEFT**"""

class MetricsLogger(TrainerCallback):
    """Callback to log metrics to WandB and prevent duplicate entries."""
    def on_log(self, args, state, control, logs=None, **kwargs):
        if logs:
            # Define qué métricas quieres loguear en WandB
            metrics_to_log = ['loss', 'eval_loss', 'eval_accuracy', 'learning_rate', 'step', 'epoch']
            # Filtra y loguea solo las métricas definidas
            wandb.log({k: v for k, v in logs.items() if k in metrics_to_log}, step=state.global_step)

    def on_epoch_end(self, args, state, control, **kwargs):
        torch.cuda.empty_cache()  # Liberar memoria al final de cada época
        print("Memoria GPU liberada al finalizar la época.")

    def on_evaluate(self, args, state, control, **kwargs):
        torch.cuda.empty_cache()  # Liberar memoria antes de la evaluación
        print("Memoria GPU liberada antes de la evaluación.")

    def on_evaluation_end(self, args, state, control, **kwargs):
        torch.cuda.empty_cache()  # Liberar memoria después de la evaluación
        print("Memoria GPU liberada después de la evaluación.")

# Función para calcular métricas
def compute_metrics(eval_pred):
    logits, labels = eval_pred
    predictions = np.argmax(logits, axis=-1)
    return {"accuracy": (predictions == labels).mean()}

# Función para guardar y registrar el modelo
def save_and_log_model(trainer, model_name, output_dir, current_datetime):
    try:
        # Guarda únicamente los pesos del modelo en el directorio especificado para su fácil recarga o distribución.
        trainer.model.save_pretrained(output_dir)

        # Guarda el modelo completo, incluyendo pesos, configuración, y opcionalmente el estado del optimizador y scheduler, ideal para reanudar el entrenamiento.
        trainer.save_model(output_dir) #

        # Limpiar el nombre del modelo para usarlo en el nombre del artefacto
        sanitized_model_name = re.sub(r"[^a-zA-Z0-9_.-]", "_", model_name)
        artifact_name = f"peft_model_{sanitized_model_name}_{current_datetime}"

        # Crear y registrar el artefacto
        model_artifact = wandb.Artifact(name=artifact_name, type="model", description=f"Fine-tuned PEFT model based on {model_name}")
        model_artifact.add_dir(output_dir)
        wandb.log_artifact(model_artifact)

    except Exception as e:
        print(f"Error during model saving or artifact logging: {e}")

def get_latest_checkpoint(output_dir):
    """
    Encuentra el checkpoint más reciente en el directorio de salida
    """
    checkpoints = glob.glob(os.path.join(output_dir, "checkpoint-*"))
    if not checkpoints:
        return None
    
    # Ordena los checkpoints por número y toma el último
    latest_checkpoint = max(checkpoints, key=lambda x: int(x.split("-")[-1]))
    return latest_checkpoint

def init_wandb(training_args, project_name, model_name, file_name, current_datetime, resume=False):
    if resume:
        try:
            # Busca el archivo wandb/latest-run/files/wandb-metadata.json
            run_id = None
            wandb_dir = os.path.join(training_args.output_dir, "wandb")
            if os.path.exists(wandb_dir):
                latest_run = os.path.join(wandb_dir, "latest-run")
                if os.path.exists(latest_run):
                    run_id = open(os.path.join(latest_run, "run_id")).read().strip()
                    print("Run_id: ", run_id)
        except Exception as e:
            print(f"No se pudo obtener el run_id anterior: {e}")
            run_id = None
    else:
        run_id = None

    wandb.init(
        project=project_name,
        name=f"Experimento_{model_name}_{file_name}_{current_datetime}",
        config=vars(training_args),
        mode="offline",
        id=run_id,
        resume="allow" if resume else "never"
    )

def train_model(model, training_args, train_dataset, eval_dataset, compute_metrics, MetricsLogger, model_name, output_dir, current_datetime):
    torch.cuda.empty_cache()
    
    # Verificar disponibilidad de GPU y memoria
    if torch.cuda.is_available():
        print(f"GPU disponible: {torch.cuda.get_device_name(0)}")
        print(f"Memoria total: {torch.cuda.get_device_properties(0).total_memory / 1e9:.2f} GB")
        print(f"Memoria disponible: {torch.cuda.memory_reserved(0) / 1e9:.2f} GB")
    
    # Buscar el último checkpoint
    latest_checkpoint = get_latest_checkpoint(output_dir)
    
    trainer = Trainer(
        model=model,
        args=training_args,
        train_dataset=train_dataset,
        eval_dataset=eval_dataset,
        compute_metrics=compute_metrics,
        callbacks=[MetricsLogger(), EarlyStoppingCallback(early_stopping_patience=2)]
    )
    
    try:
        # Si existe un checkpoint, reanudar desde ahí
        if latest_checkpoint:
            print(f"Reanudando entrenamiento desde checkpoint: {latest_checkpoint}")
            results = trainer.train(resume_from_checkpoint=latest_checkpoint)
        else:
            print("No se encontró un checkpoint. Deteniendo el programa.")
            sys.exit("Programa detenido ya que no se encontró un checkpoint.")
        
        save_and_log_model(trainer, model_name, output_dir, current_datetime)
    except RuntimeError as e:
        if "out of memory" in str(e):
            print(f"Error de memoria GPU: {e}")
            print("Intente reducir el batch size o usar gradient accumulation")
        elif "CUDA error" in str(e):
            print(f"Error de CUDA: {e}")
            print("Intente establecer CUDA_LAUNCH_BLOCKING=1 para debugging")
        else:
            print(f"Error de Runtime: {e}")
        raise
    except Exception as e:
        print(f"Ha ocurrido un error durante el entrenamiento: {e}")
        raise
    finally:
        # Limpiar memoria al finalizar
        torch.cuda.empty_cache()

    #finally:
    #    wandb.finish()

def get_device():
    if torch.cuda.is_available():
        try:
            # Probar si hay suficiente memoria en la GPU
            torch.cuda.empty_cache()
            return torch.device('cuda')
        except RuntimeError:
            print("Memoria de GPU insuficiente, cambiando a CPU.")
            return torch.device('cpu')
    else:
        return torch.device('cpu')

#device = get_device()
#print(device)

#model = model.to(device)
def configure_training(resume=False):
    lima_tz = pytz.timezone('America/Lima')
    # Si estamos reanudando, usar el mismo directorio de salida
    if resume:
        # Buscar el último directorio de resultados
        result_dirs = glob.glob("./results/run_*")
        print("result_dirs: ", result_dirs)
        if not result_dirs:
            raise ValueError("No se encontraron directorios de resultados previos")
        output_dir = max(result_dirs, key=os.path.getmtime)
        #current_datetime = output_dir.split("_")[-1]
        date = output_dir.split("_")[-2]
        time = output_dir.split("_")[-1]
        current_datetime = date+"_"+time
        print("current_datetime: ", current_datetime)
    else:
        #current_datetime = datetime.datetime.now(lima_tz).strftime("%Y%m%d_%H%M%S")
        #output_dir = f"./results/run_{current_datetime}"
        #print("current_datetime2: ", current_datetime)
        print("No se encontró un checkpoint previo. Deteniendo el programa en configure")
        sys.exit("Programa detenido porque no se encontró un checkpoint previo en configure")

    print(f"Usando directorio de salida: {output_dir}")

    training_args = TrainingArguments(
        output_dir=output_dir,
    	num_train_epochs=3,
    	learning_rate=1e-4,
    	per_device_train_batch_size=4,    # Aumentado
    	gradient_accumulation_steps=24,     # Aumentado
    	per_device_eval_batch_size=1,
        eval_strategy="steps",
    	eval_steps=500,                    # Evaluar aproximadamente 15-20 veces por época
    	save_steps=500,                    # Guardar aproximadamente 7-8 veces por época
    	logging_dir='./logs',
    	logging_steps=100,
    	fp16=True,
    	save_total_limit=1,
    	load_best_model_at_end=True,
    	metric_for_best_model="loss",
    	greater_is_better=False,
    	warmup_steps=1000,                  # Ajustado según total de steps
    	weight_decay=0.01,
        dataloader_num_workers = 8,
#        deepspeed="./deepspeed_config.json",
        gradient_checkpointing=True,
        seed=42
    )
    return training_args, output_dir, current_datetime

"""### **4.4. Ejecucion**"""

PROJECT_NAME_WANDB="FinetuningTestCases_khipu_300k"
LORA_RANK = 16  # Número de matrices de bajo rango (ajusta según tu GPU)
LORA_ALPHA = 32  # Factor de escala para LoRA
LORA_DROPOUT = 0.1  # Dropout para regularización
TASK_TYPE = TaskType.CAUSAL_LM
#DEVICE = get_device() # o 'cpu' dependiendo de tu configuración

lora_config = configure_lora(task_type=TASK_TYPE, r=LORA_RANK, lora_alpha=LORA_ALPHA, lora_dropout=LORA_DROPOUT) # Configurar LoRA
peft_model = apply_lora_to_model(model_base, lora_config) # Aplicar configuración de LoRA al modelo y moverlo al dispositivo
print_trainable_parameters(peft_model) # Imprimir parámetros entrenables

# START TRAIN
if __name__ == "__main__":
    peft_training_args, output_dir, current_datetime = configure_training(resume=True)
    init_wandb(peft_training_args, PROJECT_NAME_WANDB, MODEL_NAME, FILE_NAME, current_datetime, resume=True) #lama
    train_model(peft_model, peft_training_args, tokenized_datasets["train"],  tokenized_datasets["validation"], compute_metrics, MetricsLogger, MODEL_NAME, output_dir, current_datetime)

#wandb.finish()

"""## **Guardar y cargar modelo en HF**

### Guardar Modelo
"""

# Inicializar API
api = HfApi()

def save_model_locally(model, model_path):
    """Guarda el modelo localmente."""
    model.save_pretrained(model_path)
    tokenizer.save_pretrained(model_path)
    print(f"Modelo guardado localmente en {model_path}.")

def create_or_get_repo(repo_id, token, private=False):
    """Crea un repositorio en Hugging Face Hub si no existe."""
    try:
        api.create_repo(repo_id=repo_id, token=token, private=private, exist_ok=True)
        print(f"Repositorio listo: {repo_id}")
    except Exception as e:
        print(f"Error al crear o acceder al repositorio: {e}")

def upload_lora_adapters(repo_id, lora_model_path, token):
    """Sube los adaptadores LoRA al Hugging Face Hub."""
    api.upload_folder(repo_id=repo_id, folder_path=lora_model_path, token=token)
    print(f"Adaptadores LoRA subidos exitosamente en {repo_id}.")

def save_and_upload_quantized_model(model, model_name, tokenizer, hf_user_name, token, quantization_method="q4_k_m"):
    """Guarda el modelo en formato GGUF cuantizado y lo sube al Hugging Face Hub."""
    model_gguf_path = f"{model_name}-gguf"
    model.save_pretrained_gguf(model_gguf_path, tokenizer, quantization_method=quantization_method)
    model.push_to_hub_gguf(
        repo_id=f"{hf_user_name}/{model_gguf_path}",
        tokenizer=tokenizer,
        quantization_method=quantization_method,
        token=token
    )
    print(f"Modelo GGUF cuantizado subido en {hf_user_name}/{model_gguf_path}.")

# Configuraciones generales
HF_USERNAME = "Judiht"
HF_TOKEN = os.getenv("HUGGING_FACE_HUB_TOKEN")  # Accede al token de Hugging Face desde una variable de entorno
#FILE_NAME2 = "testcase_generator_16df"
REPO_LOCAL_NAME = f"{CLEAN_MODEL_NAME}_{FILE_NAME}_{current_datetime}"
REPO_HF_NAME = f"{HF_USERNAME}/{REPO_LOCAL_NAME}"

# Verificación del token
if HF_TOKEN is None:
    raise ValueError("El token de Hugging Face no está configurado. Asegúrate de definir HUGGING_FACE_HUB_TOKEN en las variables de entorno.")

# Flujo principal
# Guardar el modelo base localmente
save_model_locally(peft_model, REPO_LOCAL_NAME)

# Crear el repositorio en Hugging Face Hub
#create_or_get_repo(REPO_HF_NAME, HF_TOKEN)

# Subir los adaptadores LoRA
#upload_lora_adapters(REPO_HF_NAME, REPO_LOCAL_NAME, HF_TOKEN)

"""### **5 Evaluación**"""

rouge = evaluate.load('rouge')
bleu = evaluate.load("bleu")
bertscore = evaluate.load('bertscore')
sacrebleu = evaluate.load('sacrebleu')

"""### **5.1. Evaluando con Rouge**"""

# Verificamos si CUDA (GPU) está disponible
device = torch.device("cuda" if torch.cuda.is_available() else "cpu")

descriptions = tokenized_datasets['test'][0:500]['description']
human_baseline_test_case = tokenized_datasets['test'][0:500]['test_case']

base_model_test_cases = []
peft_model_test_cases = []

# Movemos los modelos al dispositivo una sola vez antes del bucle
model_base = model_base.to(device)
peft_model = peft_model.to(device)

# Aseguramos que los modelos estén en modo evaluación
model_base.eval()
peft_model.eval()

for _, description in enumerate(descriptions):
    prompt = (
        f"Generate a test JUnit for the following method description: "
        f"{description}\n\n"
        f"Test case JUnit in Java:\n"
    )

    # Tokenizamos y creamos la attention_mask
    inputs = tokenizer(prompt, return_tensors="pt", padding=True, truncation=True)
    input_ids = inputs.input_ids.to(device)
    attention_mask = inputs.attention_mask.to(device)

    # Generación con el modelo base
    with torch.no_grad():  # Desactivamos el cálculo de gradientes
        base_model_outputs = model_base.generate(
            input_ids=input_ids,
            attention_mask=attention_mask,
            max_new_tokens=200,
            pad_token_id=tokenizer.eos_token_id,
            temperature=0.7,  # Añadimos temperatura para más variabilidad
            do_sample=True,   # Activamos el muestreo aleatorio
            top_p=0.9,        # Añadimos top_p para mejor control de la generación
            num_return_sequences=1
        )
    base_model_text_output = tokenizer.decode(base_model_outputs[0], skip_special_tokens=True)
    base_model_test_cases.append(base_model_text_output)

    # Generación con el modelo PEFT
    with torch.no_grad():  # Desactivamos el cálculo de gradientes
        peft_model_outputs = peft_model.generate(
            input_ids=input_ids,
            attention_mask=attention_mask,
            max_new_tokens=200,
            pad_token_id=tokenizer.eos_token_id,
            temperature=0.7,  # Añadimos temperatura para más variabilidad
            do_sample=True,   # Activamos el muestreo aleatorio
            top_p=0.9,        # Añadimos top_p para mejor control de la generación
            num_return_sequences=1
        )
    peft_model_text_output = tokenizer.decode(peft_model_outputs[0], skip_special_tokens=True)
    peft_model_test_cases.append(peft_model_text_output)

    # Limpiamos la caché de CUDA después de cada iteración
    if torch.cuda.is_available():
        torch.cuda.empty_cache()

# Verificar que las listas tengan la misma longitud
if len(human_baseline_test_case) == len(base_model_test_cases) == len(peft_model_test_cases):
    zipped_summaries = list(zip(human_baseline_test_case, base_model_test_cases, peft_model_test_cases))
    df_report = pd.DataFrame(zipped_summaries, columns=['HUMAN_BASELINE_TEST_CASES', 'BASE_MODEL_TEST_CASES', f'PEFT_{MODEL_NAME.upper()}_MODEL_TEST_CASES'])
else:
    raise ValueError("Las listas de casos de prueba no tienen la misma longitud.")


base_model_results_rouge = rouge.compute(
    predictions=base_model_test_cases,
    references=human_baseline_test_case[0:len(base_model_test_cases)],
    use_aggregator=True,
    use_stemmer=True,
)

peft_model_results_rouge = rouge.compute(
    predictions=peft_model_test_cases,
    references=human_baseline_test_case[0:len(peft_model_test_cases)],
    use_aggregator=True,
    use_stemmer=True,
)

print('\nORIGINAL MODEL:')
print(base_model_results_rouge)

print('PEFT MODEL:')
print(peft_model_results_rouge)

"""### **5.2. Evaluando con Bleu**"""

# Evaluar el modelo base
base_model_results_bleu = bleu.compute(
    predictions=base_model_test_cases,
    references=[[ref] for ref in human_baseline_test_case[0:len(base_model_test_cases)]]
)

# Evaluar el modelo PEFT
peft_model_results_bleu = bleu.compute(
    predictions=peft_model_test_cases,
    references=[[ref] for ref in human_baseline_test_case[0:len(peft_model_test_cases)]]
)

# Imprimir resultados
print('\nBASE MODEL BLEU SCORE:')
print(base_model_results_bleu)

print('PEFT MODEL BLEU SCORE:')
print(peft_model_results_bleu)

"""### **5.3. Evaluando con BERT**"""
# BERTScore para el modelo base
base_model_results_bertscore = bertscore.compute(
    predictions=base_model_test_cases,
    references=human_baseline_test_case[0:len(base_model_test_cases)],
    lang="en"  # Cambia esto si estás usando un idioma diferente
)

# BERTScore para el modelo PEFT
peft_model_results_bertscore = bertscore.compute(
    predictions=peft_model_test_cases,
    references=human_baseline_test_case[0:len(peft_model_test_cases)],
    lang="en"
)

# Calcular promedio de cada métrica para BERTScore en el modelo base
base_model_bertscore_precision_avg = np.mean(base_model_results_bertscore['precision'])
base_model_bertscore_recall_avg = np.mean(base_model_results_bertscore['recall'])
base_model_bertscore_f1_avg = np.mean(base_model_results_bertscore['f1'])

# Calcular promedio de cada métrica para BERTScore en el modelo PEFT
peft_model_bertscore_precision_avg = np.mean(peft_model_results_bertscore['precision'])
peft_model_bertscore_recall_avg = np.mean(peft_model_results_bertscore['recall'])
peft_model_bertscore_f1_avg = np.mean(peft_model_results_bertscore['f1'])

# Imprimir resultados para verificación
print("\nBASE MODEL BERTScore Precision Avg:", base_model_bertscore_precision_avg)
print("BASE MODEL BERTScore Recall Avg:", base_model_bertscore_recall_avg)
print("BASE MODEL BERTScore F1 Avg:", base_model_bertscore_f1_avg)

print("\nPEFT MODEL BERTScore Precision Avg:", peft_model_bertscore_precision_avg)
print("PEFT MODEL BERTScore Recall Avg:", peft_model_bertscore_recall_avg)
print("PEFT MODEL BERTScore F1 Avg:", peft_model_bertscore_f1_avg)


"""### **5.4. Evaluando con SacreBLEU**"""
# SacreBLEU para el modelo base
base_model_results_sacrebleu = sacrebleu.compute(
    predictions=base_model_test_cases,
    references=[[ref] for ref in human_baseline_test_case[0:len(base_model_test_cases)]]
)

# SacreBLEU para el modelo PEFT
peft_model_results_sacrebleu = sacrebleu.compute(
    predictions=peft_model_test_cases,
    references=[[ref] for ref in human_baseline_test_case[0:len(peft_model_test_cases)]]
)

print("\nBASE MODEL SacreBLEU:", base_model_results_sacrebleu)
print("PEFT MODEL SacreBLEU:", peft_model_results_sacrebleu)


"""### **5.5. Evaluando con CodeBLEU**"""
# CodeBLEU para el modelo base
base_model_results_codebleu = calc_codebleu(
    references=[[ref] for ref in human_baseline_test_case[0:len(base_model_test_cases)]],
    predictions=base_model_test_cases,
    lang="java"  # Cambia esto si el código está en otro lenguaje
)

# CodeBLEU para el modelo PEFT
peft_model_results_codebleu = calc_codebleu(
    references=[[ref] for ref in human_baseline_test_case[0:len(peft_model_test_cases)]],
    predictions=peft_model_test_cases,
    lang="java"
)

print("\nBASE MODEL CodeBLEU:", base_model_results_codebleu)
print("PEFT MODEL CodeBLEU:", peft_model_results_codebleu)

# Registrar los resultados de ROUGE en la sección "ROUGE"
wandb.log({
    "Test/Base_Model": base_model_results_rouge,
    "Test/PEFT_Model": peft_model_results_rouge
})

# Registrar los resultados de BLEU en la sección "BLEU"
wandb.log({
    "Test/Base_Model_BLEU": base_model_results_bleu['bleu'],  # Accediendo al score directamente
    "Test/PEFT_Model_BLEU": peft_model_results_bleu['bleu']
})

# Guardar resultados promedio en WandB
wandb.log({
    "Test/Base_Model_BERTScore_Precision": base_model_bertscore_precision_avg,
    "Test/Base_Model_BERTScore_Recall": base_model_bertscore_recall_avg,
    "Test/Base_Model_BERTScore_F1": base_model_bertscore_f1_avg,
    "Test/PEFT_Model_BERTScore_Precision": peft_model_bertscore_precision_avg,
    "Test/PEFT_Model_BERTScore_Recall": peft_model_bertscore_recall_avg,
    "Test/PEFT_Model_BERTScore_F1": peft_model_bertscore_f1_avg,
})

wandb.log({
    "Test/Base_Model_SacreBLEU": base_model_results_sacrebleu['score'],
    "Test/PEFT_Model_SacreBLEU": peft_model_results_sacrebleu['score'],
    "Test/Base_Model_CodeBLEU": base_model_results_codebleu['codebleu'],
    "Test/PEFT_Model_CodeBLEU": peft_model_results_codebleu['codebleu']
})
# Finalizar sesión de WandB
wandb.finish()
torch.cuda.empty_cache()
