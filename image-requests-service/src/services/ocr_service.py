from PIL import Image
import pytesseract
import io

def extract_text_from_image(image_data: bytes) -> str:
    try:
        image = Image.open(io.BytesIO(image_data))
        text = pytesseract.image_to_string(image, lang="eng")
        return text.strip()
    except Exception as e:
        raise Exception(f"OCR processing failed: {str(e)}")