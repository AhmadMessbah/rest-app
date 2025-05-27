from beanie import Document, Indexed
from datetime import datetime
from pydantic import BaseModel
from typing import Optional

class ImageRequest(Document):
    image_data: bytes
    extracted_text: Indexed(str, index_type="text")  # Indexed for full-text search
    created_at: datetime = datetime.now()
    user_id: Optional[str] = None

    class Settings:
        name = "image_requests"
        indexes = [
            [("extracted_text", "text")]
        ]

class ImageRequestCreate(BaseModel):
    image_data: bytes
    user_id: Optional[str] = None

class ImageRequestResponse(BaseModel):
    id: str
    extracted_text: str
    created_at: datetime
    user_id: Optional[str]