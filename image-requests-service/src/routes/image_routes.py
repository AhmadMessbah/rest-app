from fastapi import APIRouter, UploadFile, File, HTTPException, Depends
from src.models.image_request import ImageRequest, ImageRequestCreate, ImageRequestResponse
from src.services.ocr_service import extract_text_from_image
from src.services.auth_service import get_current_user
from typing import List
from beanie import PydanticObjectId

router = APIRouter()

@router.post("/", summary="Extract Text From Image", response_model=ImageRequestResponse, status_code=201)
async def create_image_request(file: UploadFile = File(...), user_id: str = Depends(get_current_user)):
    if not file.content_type.startswith("image/"):
        raise HTTPException(status_code=400, detail="File must be an image")

    image_data = await file.read()
    extracted_text = extract_text_from_image(image_data)

    image_request = ImageRequest(
        image_data=image_data,
        extracted_text=extracted_text,
        user_id=user_id
    )
    await image_request.insert()

    return ImageRequestResponse(
        id=str(image_request.id),
        extracted_text=image_request.extracted_text,
        created_at=image_request.created_at,
        user_id=image_request.user_id
    )

@router.get("/{id}", response_model=ImageRequestResponse)
async def get_image_request(id: str, user_id: str = Depends(get_current_user)):
    image_request = await ImageRequest.get(PydanticObjectId(id))
    if not image_request:
        raise HTTPException(status_code=404, detail="Image request not found")
    return ImageRequestResponse(
        id=str(image_request.id),
        extracted_text=image_request.extracted_text,
        created_at=image_request.created_at,
        user_id=image_request.user_id
    )

@router.get("/", response_model=List[ImageRequestResponse])
async def get_all_image_requests(user_id: str = Depends(get_current_user)):
    image_requests = await ImageRequest.find({"user_id": user_id}).to_list()
    return [
        ImageRequestResponse(
            id=str(req.id),
            extracted_text=req.extracted_text,
            created_at=req.created_at,
            user_id=req.user_id
        ) for req in image_requests
    ]

@router.get("/search/")
async def search_image_requests(query: str, user_id: str = Depends(get_current_user)):
    image_requests = await ImageRequest.find(
        {"$text": {"$search": query}, "user_id": user_id}
    ).to_list()
    return [
        ImageRequestResponse(
            id=str(req.id),
            extracted_text=req.extracted_text,
            created_at=req.created_at,
            user_id=req.user_id
        ) for req in image_requests
    ]