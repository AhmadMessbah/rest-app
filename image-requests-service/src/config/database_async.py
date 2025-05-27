from motor.motor_asyncio import AsyncIOMotorClient
from src.config.settings import settings
from beanie import init_beanie
from src.models.image_request import ImageRequest

async def init_db():
    client = AsyncIOMotorClient(settings.MONGODB_URI)
    return client.get_database()

async def init_beanie():
    db = await init_db()
    await init_beanie(database=db, document_models=[ImageRequest])

db = None  # Will be initialized in startup_event