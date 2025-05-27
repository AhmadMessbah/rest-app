from fastapi import FastAPI, HTTPException
from src.config.database import collection
from pydantic import BaseModel
from typing import Optional
import logging

app = FastAPI()

# Configure logging
logging.basicConfig(
    filename="/logs/app.log",
    level=logging.INFO,
    format="%(asctime)s - %(name)s - %(levelname)s - %(message)s"
)
logger = logging.getLogger(__name__)

class ImageRequest(BaseModel):
    description: str
    status: Optional[str] = "pending"

@app.post("/image-requests")
async def create_request(request: ImageRequest):
    logger.info(f"Creating image request: {request.description}")
    result = collection.insert_one(request.dict())
    return {"id": str(result.inserted_id), **request.dict()}

@app.get("/image-requests/{id}")
async def get_request(id: str):
    request = collection.find_one({"_id": id})
    if not request:
        logger.error(f"Image request not found: {id}")
        raise HTTPException(status_code=404, detail="Request not found")
    logger.info(f"Retrieved image request: {id}")
    return request

@app.get("/health")
async def health_check():
    return {"status": "healthy"}