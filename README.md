# 🛠️ GenerateJUnit

Este repositorio está diseñado para la generación de casos de prueba unitarios en **JUnit** utilizando **Modelos de Lenguaje Grande (LLMs) Open Source**. Incluye todo el flujo de trabajo, desde la extracción y procesamiento de datos hasta el fine-tuning, evaluación y almacenamiento de resultados.

---

## ✨ Características Principales

- Generación de casos de prueba unitarios
- Soporte para múltiples modelos de lenguaje (GEMMA, LLaMA, Qwen)
- Preprocesamiento y limpieza de datos
- Evaluación y métricas de rendimiento
- Compatibilidad con Google Colab

## 📂 Estructura del Proyecto

```plaintext
generate-junit-tests/
├── data/                # Datos para entrenamiento
├── src/                 # Código fuente principal
│   ├── colab/           # Scripts para Google Colab
│   ├── evaluation/      # Scripts de evaluación y métricas
│   ├── finetuning/      # Scripts de fine-tuning
│   │   ├── gemma/       # Configuración de GEMMA
│   │   ├── llama/       # Configuración de LLaMA
│   │   ├── qwen/        # Configuración de Qwen
│   ├── preprocessing/   # Preparación y limpieza de datos
│   ├── results/         # Resultados generados
├── utils/               # Utilidades generales
```

### 🗂️ Carpetas y Archivos

#### `data/`  
📦 **Datos:** Contiene los datos base utilizados para el entrenamiento y evaluación de los modelos.

#### `src/`

##### `colab/`  
🌐 **Colab:** Scripts y configuraciones adaptados para ejecutar el proyecto en Google Colab.

##### `evaluation/`  
📊 **Evaluación:** Scripts para calcular métricas y realizar análisis detallados del rendimiento de los modelos después del fine-tuning.

##### `finetuning/`  
🔧 **Fine-tuning:** Scripts diseñados para ajustar modelos de lenguaje como LLaMA, Qwen y GEMMA utilizando datos personalizados.

##### `gemma/`, `llama/`, `qwen/`  
🤖 **Modelos:** Contienen configuraciones y scripts específicos para cada modelo, dependiendo de su arquitectura y necesidades.

##### `preprocessing/`  
🛠️ **Preprocesamiento:** Scripts para preparar y limpiar los datos en diferentes etapas:  
- **`1_extraction_dataset.py`**: Extrae datos relevantes del conjunto inicial.  
- **`2_make_dataset.py`**: Convierte los datos en un formato apto para el entrenamiento.  
- **`3_check_files.py`**: Valida los datos generados para garantizar su calidad.  
- **`4_generate_json.py`**: Transforma los datos a formato JSON.  
- **`5_merge_files.py`**: Fusiona archivos de datos para entrenamiento unificado.

##### `results/`  
📁 **Resultados:**  
- Resultados obtenidos con Qwen.
- Resultados obtenidos con LLaMA.

---

## 🚀 Cómo Usar Este Proyecto

### 1️⃣ **Preprocesamiento de Datos**
Ejecuta los scripts en la carpeta `preprocessing/` siguiendo este orden:  
1. `1_extraction_dataset.py`  
2. `2_make_dataset.py`  
3. `3_check_files.py`  
4. `4_generate_json.py`  
5. `5_merge_files.py`  

Esto generará un conjunto de datos limpio y listo para el entrenamiento.

### 2️⃣ **Fine-Tuning del Modelo**
Ejecuta el script `5_1_fine_tuning.py` para ajustar tu modelo elegido (como LLaMA, GEMMA o Qwen) con los datos procesados.

### 3️⃣ **Evaluación**
Analiza el rendimiento del modelo utilizando los scripts disponibles en la carpeta `evaluation/`.

### 4️⃣ **Ejecución en Google Colab**
Puedes usar los scripts en `colab/` si prefieres realizar el entrenamiento y evaluación en la nube.

---

## ⚙️ Requisitos

Asegúrate de tener instaladas las siguientes dependencias antes de ejecutar el proyecto:

- Python 3.8+
- Bibliotecas esenciales (incluye un archivo `requirements.txt`):
  - `transformers`
  - `datasets`
  - `torch`
  - `scikit-learn`
  - `pandas`
  - `evaluate`

Instala las dependencias con:

```bash
pip install -r requirements.txt
```

👩‍💻 Autor

Este proyecto fue desarrollado por [Judiht Rojas](https://github.com/cjudithrb).
