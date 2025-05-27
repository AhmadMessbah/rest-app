from motor.motor_asyncio import AsyncIOMotorClient
from src.config.settings import settings

async def init_db():
    client = AsyncIOMotorClient(settings.MONGODB_URI)
    db = client.get_database()
    return db

db = None  # Initialize in startup_event
collection = None

async def get_collection():
    if collection is None:
        raise RuntimeError("Database not initialized")
    return collection