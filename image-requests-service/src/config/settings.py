from pydantic_settings import BaseSettings

class Settings(BaseSettings):
    MONGODB_URI: str = "mongodb://mongodb:27017/image_requests_db"  # Match Docker Compose
    JWT_SECRET: str = "your_jwt_secret"
    LOGSTASH_HOST: str = ""

    class Config:
        env_file = ".env"
        env_file_encoding = "utf-8"

settings = Settings()