from pymongo import MongoClient
from src.config.settings import settings

def init_db():
    client = MongoClient(settings.MONGODB_URI)
    db = client.get_database()
    return db

db = init_db()
collection = db.image_requests