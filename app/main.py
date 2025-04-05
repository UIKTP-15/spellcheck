from fastapi import FastAPI, File, UploadFile, HTTPException
from models.CloudVision import GoogleVision
from utils.process_ocr_result import (
    sort_words, format_text, calculate_center,
    clean_unnecessary_characters, correct_brackets_in_text
)
from concurrent.futures import ThreadPoolExecutor
from typing import List, Dict
import os
import base64
import tempfile
from pydantic import BaseModel

app = FastAPI(title="OCR API", description="API for processing handwritten text from images using Google Vision.", version="1.0")

# Load Google Cloud Vision Credentials from GitHub Actions secret
SERVICE_ACCOUNT_B64 = os.getenv("GCP_SERVICE_ACCOUNT")
if SERVICE_ACCOUNT_B64:
    creds_json = base64.b64decode(SERVICE_ACCOUNT_B64).decode()
    with tempfile.NamedTemporaryFile(delete=False, suffix=".json") as temp_json:
        temp_json.write(creds_json.encode())
        temp_json_path = temp_json.name
    os.environ["GOOGLE_APPLICATION_CREDENTIALS"] = temp_json_path

# Initialize Google Vision Model
google_vision = GoogleVision()

class OCRResponse(BaseModel):
    text: str

@app.post("/detect-text/", response_model=OCRResponse, summary="Detect handwritten text in an image",
          description="Processes an uploaded image and returns detected text with coordinates and confidence scores.")
async def detect_text(files: List[UploadFile] = File(...)):
    """Uploads an image and extracts handwritten text using Google Vision OCR."""
    try:
        final_text_result = ""
        with ThreadPoolExecutor(max_workers=3) as thread_executor:
            futures = []
            for file in files:
                image_bytes = await file.read()
                futures.append(thread_executor.submit(google_vision.detect_document_text, path_or_content=image_bytes, is_local_file=False))

            for future in futures:
                detected_words = future.result()
                words_centered: List[Dict] = []
                for word, vertices, confidence in detected_words:
                    x_center, y_center = calculate_center(vertices)
                    words_centered.append({
                        "text_object_center": (x_center, y_center),
                        "word": word,
                        "confidence": confidence,
                        "vertices": vertices
                    })

                sorted_words: List[Dict] = sort_words(words_centered)
                print(sorted_words)
                correct_text: List[Dict] = clean_unnecessary_characters(sorted_words)
                formatted_text: str = format_text(correct_text)
                corrected_brackets_text: str = correct_brackets_in_text(formatted_text)
                final_text_result += f"{corrected_brackets_text}\n"
            return {"text": final_text_result}

    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

if __name__ == "__main__":
    import uvicorn
    uvicorn.run("main:app", host="127.0.0.1", port=8080, reload=True)
