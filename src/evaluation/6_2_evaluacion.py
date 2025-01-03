# -*- coding: utf-8 -*-
"""5.2.Evaluacion_v2.ipynb

Automatically generated by Colab.

Original file is located at
    https://colab.research.google.com/drive/1wlS-cY-ngnTVGEYJurVWQefNapmHd3Hi

## **1: Pre-requisitos**

### **1.1. Instalación de Dependencias**
"""

# Commented out IPython magic to ensure Python compatibility.
# %%capture
# !pip install evaluate==0.4.0 rouge-score==0.1.2 peft==0.13.2 datasets==3.0.1

"""### **1.2: Importación de Librerías**"""

import os
import re
import torch
import pandas as pd
import datetime
import pytz
import psutil
import datetime
import pytz
import time
import wandb

from datasets import Dataset
from huggingface_hub import login
from peft import PeftModel, PeftConfig
from transformers import AutoModelForCausalLM, AutoTokenizer

os.environ["WANDB_API_KEY"] = "b892949c50481e4e5ecdf7b73dbebdd22d1e5892"

from google.colab import drive
drive.mount('/content/drive')

"""## **2: Carga y Preparación del Dataset Evaluación**"""

# Definición de variables
BASE_MODEL_NAME = 'meta-llama/Llama-3.2-3B'#'google/gemma-2-2b'#'Qwen/Qwen2.5-3B'
MODEL_NAME = BASE_MODEL_NAME.split('/')[-1]
TEST_DATASET_FOLDER = '/content/drive/MyDrive/ColabNotebooks/DataEval/Gson'
INPUT_COLUMN = 'description'
OUTPUT_COLUMN = 'test_case'
OUTPUT_DIR = '/content/DataEvalOutput'
BATCH_SIZE = 4
BEAM_SIZE = 4
MAX_NEW_TOKENS = 256
MAX_LENGTH = 512
GPU_NUM = '0'

"""### **2.1. Carga de archivos de evaluación CSV**"""

def list_csv_files(folder_path):
    """
    Lista todos los archivos CSV en un directorio específico.

    Args:
        folder_path (str): Ruta al directorio donde se encuentran los archivos CSV.

    Returns:
        list: Lista de rutas de archivos CSV.
    """
    return [file for file in os.listdir(folder_path) if file.endswith(".csv")]

def load_and_concat_csv_files(csv_files, folder_path):
    """
    Lee y concatena archivos CSV desde un directorio específico en un solo DataFrame.

    Args:
        csv_files (list): Lista de nombres de archivos CSV.
        folder_path (str): Ruta al directorio que contiene los archivos CSV.

    Returns:
        pd.DataFrame: DataFrame combinado de todos los archivos CSV.
    """
    all_data = []
    for csv_file in csv_files:
        file_path = os.path.join(folder_path, csv_file)
        data = pd.read_csv(file_path, delimiter='\t')
        all_data.append(data)
    if all_data:
        combined_data = pd.concat(all_data, ignore_index=True)
        print(f"{len(csv_files)} archivos CSV cargados y concatenados con éxito.")
        return combined_data
    else:
        print("No se encontraron archivos CSV o no se cargaron datos.")
        return pd.DataFrame()

def main_load_csv(test_folder):
    # Listar y cargar archivos CSV
    csv_files = list_csv_files(test_folder)
    if not csv_files:
        print("No se encontraron archivos CSV en el directorio especificado.")
        return

    df_test = load_and_concat_csv_files(csv_files, test_folder)

    if df_test.empty:
        print("No se cargaron datos válidos. Verifique los archivos CSV.")
        return

    return df_test

# Función para extraer la clase, el método y la descripción de una línea de texto
def extract_details(row):
    class_match = re.search(r'Class:\s*(\S+)', row)
    method_match = re.search(r'Method:\s*(\S+)', row)
    description_match = re.search(r'Description:\s*(.*?)$', row, re.DOTALL)

    extracted = {
        'Class': class_match.group(1) if class_match else None,
        'Method': method_match.group(1) if method_match else None,
        'Description': description_match.group(1).strip() if description_match else None
    }
    return pd.Series(extracted)

def add_prompt(df, column_name, text):
    df[column_name] = text
    return df

"""### **2.2. Carga del modelo para evaluación**"""

# Replace "YOUR_HUGGING_FACE_TOKEN" with your actual token
login(token="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")

def get_device():
    """Configura y retorna el dispositivo basado en la disponibilidad de CUDA."""
    device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
    print(f"Usando dispositivo: {device}")
    return device

def load_model_and_tokenizer(base_model_name, is_peft=False, peft_repo=None):
    """
    Carga un modelo (base o PEFT) y el tokenizador correspondiente, moviéndolos al dispositivo adecuado.

    Args:
        base_model_name (str): Nombre o ruta del modelo base preentrenado.
        is_peft (bool): Indica si se debe cargar un modelo PEFT.
        peft_repo (str): Ruta o nombre del repositorio PEFT si is_peft es True.

    Returns:
        model, tokenizer: El modelo y el tokenizador cargados en el dispositivo.
    """
    device = get_device()

    # Cargar el modelo base
    model = AutoModelForCausalLM.from_pretrained(base_model_name, device_map="auto")

    if is_peft and peft_repo:
        # Cargar configuración PEFT y crear el modelo PEFT
        config = PeftConfig.from_pretrained(peft_repo)
        #model = PeftModel.from_pretrained(model, peft_repo).to(device)
        model = PeftModel.from_pretrained(model, peft_repo, device_map={"": device}) # Esta línea fue modificada, device_map para el modelo PEFT
        print("Modelo PEFT cargado correctamente.")
        tokenizer_name = peft_repo  # Utilizar el nombre del repositorio PEFT para el tokenizador
    else:
        tokenizer_name = base_model_name  # Usar el nombre del modelo base para el tokenizador

    # Cargar el tokenizador y establecer el pad_token
    tokenizer = AutoTokenizer.from_pretrained(tokenizer_name, use_fast=True, padding_side='left', clean_up_tokenization_spaces=True)
    tokenizer.pad_token = tokenizer.eos_token
    print("Modelo y tokenizador cargados exitosamente.")

    return model, tokenizer

"""## **3: Evaluando con tecnicas de prompting**"""

lima_tz = pytz.timezone('America/Lima')
current_datetime = datetime.datetime.now(lima_tz).strftime("%Y%m%d_%H%M%S")

# Carga de los datos de prueba
data_test = main_load_csv(TEST_DATASET_FOLDER)
if not data_test.empty:
    data_test[['Class', 'Method', 'OnlyDescription']] = data_test[INPUT_COLUMN].apply(extract_details)
    data_test = add_prompt(data_test, 'Prompt', "Write a JUnit test method for the following Java method")

data_test.head()

# Carga de modelos
model_base, tokenizer_base = load_model_and_tokenizer(BASE_MODEL_NAME)

REPO_HF_NAME = "Judiht/Qwen2_5-3B_d234k_20241109" #"Judiht/Qwen2_5-3B_d225k_20241109" #"Judiht/Qwen2_5-3B_d234k_20241109" OK
model_peft, tokenizer_peft = load_model_and_tokenizer(BASE_MODEL_NAME, is_peft=True, peft_repo=REPO_HF_NAME)

"""### **Util**"""

def save_outputs_by_class(data, generated_outputs, output_dir="./outputs"):
    """
    Guarda los resultados generados en archivos separados por cada ClassName,
    asegurando que se guarden múltiples ejemplos.

    Args:
        data (pd.DataFrame): DataFrame con los datos de entrada.
        generated_outputs (list): Lista de salidas generadas por el modelo.
        output_dir (str): Directorio donde se guardarán los archivos de salida.
    """
    os.makedirs(output_dir, exist_ok=True)
    outputs_by_class = {}

    if len(data) != len(generated_outputs):
        print(f"Advertencia: La longitud de los datos ({len(data)}) y las salidas generadas ({len(generated_outputs)}) no coinciden.")
        min_length = min(len(data), len(generated_outputs))
        data = data.iloc[:min_length]
        generated_outputs = generated_outputs[:min_length]

    for i, (index, row) in enumerate(data.iterrows()):
        class_name = row.get('Class', 'Unknown')  # Maneja el caso en que no exista 'Class'
        if not class_name or class_name == 'None':
            match = re.search(r'Class\s*:\s*(\w+)', generated_outputs[i])
            class_name = match.group(1) if match else 'Unknown'

        outputs_by_class.setdefault(class_name, []).append(generated_outputs[i])

    for class_name, outputs in outputs_by_class.items():
        filename = os.path.join(output_dir, f"{class_name}_test_cases.txt")
        with open(filename, 'a') as f:
            f.writelines(f"{output}\n\n" for output in outputs)
        print(f"Archivo guardado para la clase {class_name}: {filename}")

def generate_test_case(prompts, model, tokenizer, max_new_tokens=256):
    """
    Genera casos de prueba a partir de una lista de prompts usando el modelo y el tokenizador.

    Args:
        prompts (list): Lista de prompts generados.
        model: Modelo de lenguaje para generación de texto.
        tokenizer: Tokenizador para preparar la entrada del modelo.
        max_new_tokens (int): Número máximo de tokens generados.

    Returns:
        list: Casos de prueba generados por el modelo.
    """
    outputs = []
    for prompt in prompts:
        inputs = tokenizer(prompt, return_tensors='pt').to(model.device)
        output = model.generate(
            inputs["input_ids"],
            attention_mask=inputs["attention_mask"],
            max_new_tokens=max_new_tokens,
            pad_token_id=tokenizer.eos_token_id
        )
        outputs.append(tokenizer.decode(output[0], skip_special_tokens=True))
    return outputs

def extract_and_save_java_code_only(file_path, output_file_path):
    """
    Lee un archivo, extrae solo los bloques de código Java después de 'JUnit en Java:',
    y los guarda en un nuevo archivo, omitiendo descripciones u otros textos.

    Args:
        file_path (str): Ruta al archivo de entrada.
        output_file_path (str): Ruta al archivo de salida.
    """
    with open(file_path, 'r') as file:
        content = file.read()

    matches = re.findall(r"JUnit en Java:\n(.*?)(?=\n===|$)", content, re.DOTALL)
    extracted_codes = [match.strip() for match in matches]

    with open(output_file_path, 'w') as output_file:
        for i, code in enumerate(extracted_codes, 1):
            code_lines = [
                line for line in code.split('\n')
                if not line.startswith("Paso") and not line.startswith("Descripción")
                and not line.strip().startswith("Class:") and not line.strip().startswith("Method:")
            ]
            clean_code = '\n'.join(code_lines).strip()
            if clean_code:
                output_file.write(f"=== Extracted Code #{i} ===\n{clean_code}\n\n")
    print(f"Bloques de código limpios guardados en: {output_file_path}")

torch.cuda.empty_cache()

"""### **3.2. Few-Shot Prompting**"""

dataset_calculadora = [
    {
        "class": "Calculator",
        "method": "add",
        "description": "Este método recibe dos enteros y devuelve su suma.",
        "junit_test_case": """
            @Test
            public void testAdd() {
                Calculator calc = new Calculator();
                assertEquals(5, calc.add(2, 3));
                assertEquals(-1, calc.add(2, -3));
                assertEquals(0, calc.add(0, 0));
            }
        """
    },
    {
        "class": "Calculator",
        "method": "subtract",
        "description": "Este método recibe dos enteros y devuelve la resta del segundo al primero.",
        "junit_test_case": """
            @Test
            public void testSubtract() {
                Calculator calc = new Calculator();
                assertEquals(-1, calc.subtract(2, 3));
                assertEquals(5, calc.subtract(2, -3));
                assertEquals(0, calc.subtract(0, 0));
            }
        """
    },
    {
        "class": "Calculator",
        "method": "multiply",
        "description": "Este método recibe dos enteros y devuelve su producto.",
        "junit_test_case": """
            @Test
            public void testMultiply() {
                Calculator calc = new Calculator();
                assertEquals(6, calc.multiply(2, 3));
                assertEquals(-6, calc.multiply(2, -3));
                assertEquals(0, calc.multiply(0, 3));
            }
        """
    },
    {
        "class": "Calculator",
        "method": "divide",
        "description": "Este método recibe dos enteros y devuelve el resultado de dividir el primero por el segundo. Lanza una ArithmeticException si el divisor es cero.",
        "junit_test_case": """
            @Test
            public void testDivide() {
                Calculator calc = new Calculator();
                assertEquals(2, calc.divide(6, 3));
                assertEquals(-2, calc.divide(6, -3));
                assertThrows(ArithmeticException.class, () -> calc.divide(6, 0));
            }
        """
    },
    {
        "class": "Calculator",
        "method": "power",
        "description": "Este método recibe dos enteros y devuelve el primer número elevado a la potencia del segundo.",
        "junit_test_case": """
            @Test
            public void testPower() {
                Calculator calc = new Calculator();
                assertEquals(8, calc.power(2, 3));
                assertEquals(1, calc.power(2, 0));
                assertEquals(0.25, calc.power(2, -2), 0.001);
            }
        """
    }
]

def make_few_shot_prompt(dataset, num_examples):
    """
    Genera un prompt few-shot utilizando ejemplos de un dataset de pruebas.

    Args:
        dataset (list): Lista de ejemplos, cada uno con los campos 'class', 'method', 'description', y 'junit_test_case'.
        num_examples (int): Número de ejemplos a incluir en el prompt.

    Returns:
        str: Prompt few-shot generado.
    """
    # Asegurarse de no exceder el tamaño del dataset
    num_examples = min(num_examples, len(dataset))

    # Construcción del prompt few-shot
    prompt_lines = ["Genera un test unitario en JUnit para cada método descrito a continuación.\n\n"]
    for i in range(num_examples):
        example = dataset[i]
        prompt_lines.extend([
            f"Clase: {example['class']}\n",
            f"Método: {example['method']}\n",
            f"Descripción: {example['description']}\n",
            f"JUnit Test Case:\n{example['junit_test_case']}\n\n"
        ])

    return ''.join(prompt_lines)

def generate_test_case_prompt_with_few_shot(data_test, num_records, few_shot_prompt):
    """
    Genera un prompt para creación de casos de prueba JUnit a partir de un dataset dado,
    usando ejemplos de few-shot como contexto.

    Args:
        data_test (list): Lista de ejemplos con los campos 'class', 'method', y 'description'.
        num_records (int): Número de registros a incluir en el prompt.
        few_shot_prompt (str): Prompt few-shot generado con ejemplos de referencia.

    Returns:
        str: Prompt completo para generación de casos de prueba JUnit.
    """
    # Asegurarse de no exceder el tamaño del dataset
    num_records = min(num_records, len(data_test))

    # Construcción del prompt, comenzando con el prompt few-shot
    prompt_lines = [few_shot_prompt, "Ahora genera test cases para los siguientes métodos:\n\n"]
    for i in range(num_records):
        record = data_test[i]
        prompt_lines.extend([
            f"Clase: {record['class']}\n",
            f"Método: {record['method']}\n",
            f"Descripción: {record['description']}\n",
            "JUnit Test Case:\n"  # El modelo generará el contenido
        ])

    return ''.join(prompt_lines)

# Generar el prompt few-shot con ejemplos de dataset_calculadora
few_shot_prompt = make_few_shot_prompt(dataset_calculadora, num_examples=2)

# Usar el prompt few-shot como contexto para generar test cases en data_test
generated_outputs_fewshot = generate_test_case_prompt_with_few_shot(data_test, num_records=len(data_test), few_shot_prompt=few_shot_prompt)

# Mostrar el prompt completo
print(f"TEST CASE WITH FEW-SHOT :\n{test_case_prompt}\n{'='*80}")

NAME_TEC_PROMPT = '/FewShot'
OUTPUT_DIR_FULL = CONFIG['TEST_DATASET_FOLDER'] + '/DataEvalOutput_'+current_datetime + NAME_TEC_PROMPT
os.makedirs(OUTPUT_DIR_FULL, exist_ok=True)

# Ruta al archivo de entrada generado por save_outputs_by_class
#FILE_PATH = OUTPUT_DIR_FULL + '/CSVParser_test_cases.txt' # Ruta al archivo de entrada generado por save_outputs_by_class
#OUTPUT_FILE_PATH = '/CSVParserTest.txt' # Ruta al archivo de salida donde se guardarán los códigos extraídos
save_outputs_by_class(data_test, generated_outputs_fewshot, output_dir=OUTPUT_DIR_FULL)

# Recorrer todos los archivos en el directorio OUTPUT_DIR_FULL
for filename in os.listdir(OUTPUT_DIR_FULL):
    # Verificar si el archivo termina en '_test_cases.txt'
    if filename.endswith('_test_cases.txt'):
        # Construir la ruta completa del archivo de entrada
        file_path = os.path.join(OUTPUT_DIR_FULL, filename)

        # Construir la ruta de salida con el sufijo "Test" en lugar de "_test_cases.txt"
        output_file_path = os.path.join(OUTPUT_DIR_FULL, filename.replace('_test_cases.txt', 'Test.txt'))

        # Ejecutar la función en el archivo actual
        extract_and_save_java_code_only(file_path, output_file_path)
        print(f'Archivo procesado y guardado en {output_file_path}')

"""### **3.3. Program of Thoughts (PoT) Prompting**"""

df = pd.read_csv('/content/ExtendedBufferedReader_methods.csv')
df['Class'] = 'ExtendedBufferedReader'
df = df.rename(columns={'name': 'Method'})
df.head()

def make_prompt_one_or_few_shot_pot(data_test, num_examples, start_index=0):
    """
    Genera una lista de prompts de tipo Program of Thoughts para un conjunto de ejemplos del dataset.

    Args:
        data_test (pd.DataFrame): DataFrame de entrada con las columnas necesarias.
        num_examples (int): Número de ejemplos para generar prompts.
        start_index (int): Índice a partir del cual comenzar a extraer ejemplos.

    Returns:
        list: Lista de prompts generados.
    """
    total_examples = len(data_test)
    num_examples = min(num_examples, total_examples - start_index)  # Ajustar el número de ejemplos

    # Construcción de prompts usando listas para concatenación eficiente
    prompts = [
        (
            f"Genera un test unitario para el método siguiente siguiendo estos pasos:\n\n"
            f"Paso 1: Lee y comprende la descripción del método.\n"
            f"Descripción del método: {data_test.iloc[i]['OnlyDescription']}\n"
            f"Paso 2: Identifica el comportamiento esperado del método.\n"
            f"Paso 3: Construye una clase de prueba JUnit en Java que verifique este comportamiento.\n"
            f"Paso 4: Incluye declaraciones de aserción relevantes en el método de prueba. Y asegurate de usar @Test\n\n"
            f"Class: {data_test.iloc[i]['Class']}\n"
            f"Method: {data_test.iloc[i]['Method']}\n"
            "JUnit en Java:\n"
        )
        for i in range(start_index, start_index + num_examples)
    ]
    return prompts

start_time = time.time()
# Inicializar W&B
wandb.finish()
wandb.init(
    project="Inf_Test_cases_CSV",
    name=f"PromptingPoT_{MODEL_NAME}_{current_datetime}",
    config={
        "num_examples": len(data_test),
        "model_name": BASE_MODEL_NAME,
        "description": "Generación de prompts estilo PoT"
    }
)

# Generación de los prompts y test cases
n = len(data_test)
prompts = make_prompt_one_or_few_shot_pot(data_test, n)
generated_outputs_pot = generate_test_case(prompts, model_base, tokenizer_base)

# Loggear la duración del proceso de generación
duration = time.time() - start_time
wandb.log({"generation_duration_seconds": duration})

# Mostrar los resultados generados
for i, (prompt, output) in enumerate(zip(prompts, generated_outputs_pot), 1):
    print(f"PoT PROMPT #{i}:\n{prompt}\n{'='*80}")
    print(f"MODEL GENERATION #{i}:\n{output}\n")

start_time = time.time()
# Inicializar W&B
wandb.init(
    project="Inf_Test_cases_GSON",
    name=f"PromptingPoT_{MODEL_NAME}_{current_datetime}",
    config={
        "num_examples": len(data_test),
        "model_name": BASE_MODEL_NAME,
        "description": "Generación de prompts estilo PoT"
    }
)

# Generación de los prompts y test cases
n = len(data_test)
prompts = make_prompt_one_or_few_shot_pot(data_test, n)
generated_outputs_pot = generate_test_case(prompts, model_base, tokenizer_base)

# Loggear la duración del proceso de generación
duration = time.time() - start_time
wandb.log({"generation_duration_seconds": duration})

# Mostrar los resultados generados
for i, (prompt, output) in enumerate(zip(prompts, generated_outputs_pot), 1):
    print(f"PoT PROMPT #{i}:\n{prompt}\n{'='*80}")
    print(f"MODEL GENERATION #{i}:\n{output}\n")

wandb.finish()

NAME_TEC_PROMPT = '/PoT'
#OUTPUT_DIR_FULL = CONFIG['OUTPUT_DIR'] + NAME_TEC_PROMPT
OUTPUT_DIR_FULL = TEST_DATASET_FOLDER + '/Llama'+current_datetime + NAME_TEC_PROMPT
os.makedirs(OUTPUT_DIR_FULL, exist_ok=True)

# Ruta al archivo de entrada generado por save_outputs_by_class
#FILE_PATH = OUTPUT_DIR_FULL + '/CSVParser_test_cases.txt' # Ruta al archivo de entrada generado por save_outputs_by_class
#OUTPUT_FILE_PATH = '/CSVParserTest.txt' # Ruta al archivo de salida donde se guardarán los códigos extraídos
save_outputs_by_class(data_test, generated_outputs_pot, output_dir=OUTPUT_DIR_FULL)

# Recorrer todos los archivos en el directorio OUTPUT_DIR_FULL
for filename in os.listdir(OUTPUT_DIR_FULL):
    # Verificar si el archivo termina en '_test_cases.txt'
    if filename.endswith('_test_cases.txt'):
        # Construir la ruta completa del archivo de entrada
        file_path = os.path.join(OUTPUT_DIR_FULL, filename)

        # Construir la ruta de salida con el sufijo "Test" en lugar de "_test_cases.txt"
        output_file_path = os.path.join(OUTPUT_DIR_FULL, filename.replace('_test_cases.txt', 'Test.txt'))

        # Ejecutar la función en el archivo actual
        extract_and_save_java_code_only(file_path, output_file_path)
        print(f'Archivo procesado y guardado en {output_file_path}')

# Finaliza la ejecución de W&B
wandb.finish()

!wandb sync /content/wandb/run-20241115_225615-v07ztqyx

"""### **3.4. Chain-of-Code (CoC) Prompting**"""

def make_prompt_one_or_few_shot_coc(datatest, num_examples, start_index=0):
    """
    Genera una lista de prompts en estilo Chain-of-Code para un conjunto de ejemplos del dataset.

    Args:
        tokenized_dataset (dict): Dataset tokenizado con descripciones y casos de prueba.
        num_examples (int): Número de ejemplos para generar prompts.
        start_index (int): Índice a partir del cual comenzar a extraer ejemplos.
        class_name (str): Nombre de la clase en la cual se encuentra el método.
        method_name (str): Nombre del método para el cual se genera el test.

    Returns:
        list: Lista de prompts generados en estilo Chain-of-Code.
    """
    total_examples = len(datatest)
    num_examples = min(num_examples, total_examples - start_index)

    # Crear prompts en estilo Chain-of-Code
    prompts = [
        (
            f"Genera un test unitario JUnit en Java para el método descrito a continuación.\n"
            f"Clase: {datatest.iloc[i]['Class']}\n"
            f"Method: {datatest.iloc[i]['Method']}\n"
            f"Descripción: {datatest.iloc[i]['OnlyDescription']}\n\n"
            "Test JUnit en Java:\n"
        )
        for i in range(start_index, start_index + num_examples)
    ]

    return prompts

start_time = time.time()
# Inicializar W&B
wandb.init(
    project="Inf_Test_cases_GSON",
    name=f"PromptingCoC_{MODEL_NAME}_{current_datetime}",
    config={
        "num_examples": len(data_test),
        "model_name": BASE_MODEL_NAME,
        "description": "Generación de prompts estilo CoC"
    }
)

# Generación de los prompts y test cases
prompts = make_prompt_one_or_few_shot_coc(data_test, num_examples=len(data_test))
#generated_outputs_coc = generate_test_case(prompts, model_peft, tokenizer_peft)
generated_outputs_coc = generate_test_case(prompts, model_base, tokenizer_base)

# Loggear la duración del proceso de generación
duration = time.time() - start_time
wandb.log({"generation_duration_seconds": duration})

# Mostrar resultados
for i, (prompt, output) in enumerate(zip(prompts, generated_outputs_coc), 1):
    print(f"CoC PROMPT #{i}:\n{prompt}\n{'='*80}")
    print(f"MODEL GENERATION #{i}:\n{output}\n")

NAME_TEC_PROMPT = '/CoC'
#OUTPUT_DIR_FULL = CONFIG['OUTPUT_DIR'] + NAME_TEC_PROMPT
OUTPUT_DIR_FULL = TEST_DATASET_FOLDER + '/Llama'+current_datetime + NAME_TEC_PROMPT
os.makedirs(OUTPUT_DIR_FULL, exist_ok=True)

save_outputs_by_class(data_test, generated_outputs_coc, output_dir=OUTPUT_DIR_FULL)

# Recorrer todos los archivos en el directorio OUTPUT_DIR_FULL
for filename in os.listdir(OUTPUT_DIR_FULL):
    # Verificar si el archivo termina en '_test_cases.txt'
    if filename.endswith('_test_cases.txt'):
        # Construir la ruta completa del archivo de entrada
        file_path = os.path.join(OUTPUT_DIR_FULL, filename)

        # Construir la ruta de salida con el sufijo "Test" en lugar de "_test_cases.txt"
        output_file_path = os.path.join(OUTPUT_DIR_FULL, filename.replace('_test_cases.txt', 'Test.txt'))

        # Ejecutar la función en el archivo actual
        extract_and_save_java_code_only(file_path, output_file_path)
        print(f'Archivo procesado y guardado en {output_file_path}')

# Finaliza la ejecución de W&B
wandb.finish()

"""### **3.5. Tree-of-Thoughts (ToT) Prompting**"""

def make_prompt_one_or_few_shot_tot(data_test, num_examples, start_index=0):
    """
    Genera una lista de prompts en estilo Tree-of-Thoughts para un conjunto de ejemplos del dataset.

    Args:
        tokenized_dataset: Dataset tokenizado que contiene descripciones y casos de prueba.
        num_examples: Número de ejemplos para generar prompts.
        start_index: Índice a partir del cual comenzar a extraer ejemplos.

    Returns:
        Una lista de prompts generados.
    """
    prompts = []
    total_examples = len(data_test)

    # Asegura que el número de ejemplos no exceda la longitud del dataset
    num_examples = min(num_examples, total_examples - start_index)

    for i in range(start_index, start_index + num_examples):
        #test_example = data_test[i]
        description = {data_test.iloc[i]['OnlyDescription']}

        # Prompt en formato Tree-of-Thoughts
        prompt = (
            f"Considera las siguientes opciones para generar un test case JUnit para el siguiente método: {data_test.iloc[i]['Method']}\n"
            f"Con descripcion: {description}\n"
            f"Opción 1: Crear un test que verifique el valor de retorno esperado del método.\n"
            f"Opción 2: Crear un test que verifique el comportamiento del método ante diferentes entradas.\n"
            f"Opción 3: Crear un test que evalúe los efectos secundarios del método (si modifica algún estado).\n\n"
            f"Genera el test case JUnit con las opciones anteriores como guía:\n"
        )

        prompt2 = (
            f"Imagina que deseas escribir un test unitario en JUnit para el siguiente método de la clase:\n\n"
            f"Clase: {data_test.iloc[i]['Class']}\n"
            f"Método: {data_test.iloc[i]['Method']}\n"
            f"Descripción del método: {description}\n\n"
            "Considera las siguientes opciones para crear un test efectivo:\n\n"
            "Opción 1: Verificar el valor de retorno esperado del método.\n"
            "- Define el resultado esperado para diferentes entradas del método.\n"
            "- Asegúrate de incluir tanto los casos típicos como los casos límite.\n\n"
            "Opción 2: Evaluar el comportamiento del método ante diferentes entradas.\n"
            "- Considera posibles entradas válidas e inválidas que el método podría recibir.\n"
            "- Asegúrate de verificar que el método maneje errores o valores atípicos correctamente.\n\n"
            "Opción 3: Evaluar los efectos secundarios del método.\n"
            "- Si el método modifica algún estado o variable global, verifica que los cambios sean los esperados.\n"
            "- Asegúrate de que no existan efectos secundarios no deseados.\n\n"
            "Usando las opciones anteriores como guía, genera un test case en JUnit que considere todos los aspectos relevantes para validar el método adecuadamente. Recuerda incluir aserciones claras y bien definidas para cada opción seleccionada en el test:\n"
        )
        prompts.append(prompt)

    return prompts

start_time = time.time()
# Inicializar W&B
wandb.init(
    project="Inf_Test_cases_GSON",
    name=f"PromptingToT_{MODEL_NAME}_{current_datetime}",
    config={
        "num_examples": len(data_test),
        "model_name": BASE_MODEL_NAME,
        "description": "Generación de prompts estilo ToT"
    }
)

# Generación de los prompts y test cases
n = len(data_test)
prompts = make_prompt_one_or_few_shot_tot(data_test, num_examples=n)
#generated_outputs_tot = generate_test_case(prompts, model_peft, tokenizer_peft)
generated_outputs_tot = generate_test_case(prompts, model_base, tokenizer_base)

# Loggear la duración del proceso de generación
duration = time.time() - start_time
wandb.log({"generation_duration_seconds": duration})

# Mostrar resultados
for i, (prompt, output) in enumerate(zip(prompts, generated_outputs_tot), 1):
    print(f"ToT PROMPT #{i}:\n{prompt}\n{'='*80}")
    print(f"MODEL GENERATION #{i}:\n{output}\n")

start_time = time.time()
# Inicializar W&B
wandb.init(
    project="Inf_Test_cases_GSON",
    name=f"PromptingToT_{MODEL_NAME}_{current_datetime}",
    config={
        "num_examples": len(data_test),
        "model_name": BASE_MODEL_NAME,
        "description": "Generación de prompts estilo ToT"
    }
)

# Generación de los prompts y test cases
n = len(data_test)
prompts = make_prompt_one_or_few_shot_tot(data_test, num_examples=n)
#generated_outputs_tot = generate_test_case(prompts, model_peft, tokenizer_peft)
generated_outputs_tot = generate_test_case(prompts, model_base, tokenizer_base)

# Loggear la duración del proceso de generación
duration = time.time() - start_time
wandb.log({"generation_duration_seconds": duration})

# Mostrar resultados
for i, (prompt, output) in enumerate(zip(prompts, generated_outputs_tot), 1):
    print(f"ToT PROMPT #{i}:\n{prompt}\n{'='*80}")
    print(f"MODEL GENERATION #{i}:\n{output}\n")

wandb.finish()

NAME_TEC_PROMPT = '/ToT'
#OUTPUT_DIR_FULL = CONFIG['OUTPUT_DIR'] + NAME_TEC_PROMPT
OUTPUT_DIR_FULL = TEST_DATASET_FOLDER + '/Llama'+current_datetime + NAME_TEC_PROMPT
os.makedirs(OUTPUT_DIR_FULL, exist_ok=True)

save_outputs_by_class(data_test, generated_outputs_tot, output_dir=OUTPUT_DIR_FULL)

# Recorrer todos los archivos en el directorio OUTPUT_DIR_FULL
for filename in os.listdir(OUTPUT_DIR_FULL):
    # Verificar si el archivo termina en '_test_cases.txt'
    if filename.endswith('_test_cases.txt'):
        # Construir la ruta completa del archivo de entrada
        file_path = os.path.join(OUTPUT_DIR_FULL, filename)

        # Construir la ruta de salida con el sufijo "Test" en lugar de "_test_cases.txt"
        output_file_path = os.path.join(OUTPUT_DIR_FULL, filename.replace('_test_cases.txt', 'Test.txt'))

        # Ejecutar la función en el archivo actual
        extract_and_save_java_code_only(file_path, output_file_path)
        print(f'Archivo procesado y guardado en {output_file_path}')

# Finaliza la ejecución de W&B
wandb.finish()