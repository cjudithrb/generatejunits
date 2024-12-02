import os
from huggingface_hub import HfApi

# Inicializar API de Hugging Face
api = HfApi()

def create_or_get_repo(repo_id, token, private=False):
    """Crea un repositorio en Hugging Face Hub si no existe."""
    try:
        api.create_repo(repo_id=repo_id, token=token, private=private, exist_ok=True)
        print(f"Repositorio listo: {repo_id}")
    except Exception as e:
        print(f"Error al crear o acceder al repositorio {repo_id}: {e.__class__.__name__}: {e}")

def upload_lora_adapters(repo_id, lora_model_path, token):
    """Sube los adaptadores LoRA al Hugging Face Hub."""
    try:
        if not os.path.exists(lora_model_path):
            raise FileNotFoundError(f"La ruta especificada '{lora_model_path}' no existe.")
        api.upload_folder(repo_id=repo_id, folder_path=lora_model_path, token=token)
        print(f"Adaptadores LoRA subidos exitosamente en {repo_id}.")
    except Exception as e:
        print(f"Error al subir los adaptadores LoRA a {repo_id}: {e.__class__.__name__}: {e}")

def save_and_upload_quantized_model(model, model_name, tokenizer, hf_user_name, token, quantization_method="q4_k_m"):
    """Guarda el modelo en formato GGUF cuantizado y lo sube al Hugging Face Hub."""
    try:
        model_gguf_path = f"{model_name}-gguf"
        # Asumiendo que model tiene los métodos definidos apropiadamente
        model.save_pretrained_gguf(model_gguf_path, tokenizer, quantization_method=quantization_method)
        model.push_to_hub_gguf(
            repo_id=f"{hf_user_name}/{model_gguf_path}",
            tokenizer=tokenizer,
            quantization_method=quantization_method,
            token=token
        )
        print(f"Modelo GGUF cuantizado subido en {hf_user_name}/{model_gguf_path}.")
    except AttributeError as e:
        print(f"Error: El modelo proporcionado no tiene el método necesario: {e.__class__.__name__}: {e}")
    except Exception as e:
        print(f"Error al guardar y subir el modelo GGUF cuantizado: {e.__class__.__name__}: {e}")

# Configuraciones generales
HF_USERNAME = "Judiht"
HF_TOKEN = os.getenv("HUGGING_FACE_HUB_TOKEN")
REPO_LOCAL_NAME = "Qwen2_5-3B_dataset350k_20241109_182244"
REPO_HF_NAME = "Qwen2_5-3B_d225k_20241109_182244"
REPO_HF_FULLNAME = f"{HF_USERNAME}/{REPO_HF_NAME}"

# Verificación del token
if HF_TOKEN is None:
    raise ValueError("El token de Hugging Face no está configurado. Asegúrate de definir HUGGING_FACE_HUB_TOKEN en las variables de entorno.")

# Crear el repositorio en Hugging Face Hub
create_or_get_repo(REPO_HF_FULLNAME, HF_TOKEN)

# Subir los adaptadores LoRA
upload_lora_adapters(REPO_HF_FULLNAME, REPO_LOCAL_NAME, HF_TOKEN)
