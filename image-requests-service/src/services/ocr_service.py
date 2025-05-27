import pytesseract
from PIL import Image
import io


def extract_text_from_image(image_data: bytes) -> str:
    """
    Extract text from an image using pytesseract.

    Args:
        image_data (bytes): Binary data of the image.

    Returns:
        str: Extracted text from the image.
    """
    try:
        # Convert bytes to PIL Image
        image = Image.open(io.BytesIO(image_data))

        # Perform OCR to extract text
        extracted_text = pytesseract.image_to_string(image, lang='eng')

        # Clean up extracted text (remove extra whitespace, newlines)
        cleaned_text = ' '.join(extracted_text.split())

        return cleaned_text if cleaned_text else "No text found in image"
    except Exception as e:
        raise Exception(f"Error extracting text from image: {str(e)}")