from fastapi import FastAPI
from src.routes.image_routes import router as image_router
from src.config.database import init_db

app = FastAPI()

app.include_router(image_router)

@app.get("/health")
async def health_check():
    return {"status": "healthy"}

@app.on_event("startup")
def startup_event():
    init_db()  # Synchronous call, no 'await'

# Temporary token generator for testing
from fastapi import APIRouter
from jose import jwt
from src.config.settings import settings

auth_router = APIRouter(prefix="/auth", tags=["auth"])

@auth_router.post("/token")
async def generate_token(user_id: str):
    token = jwt.encode({"sub": user_id}, settings.JWT_SECRET, algorithm="HS256")
    return {"access_token": token, "token_type": "bearer"}

app.include_router(auth_router)