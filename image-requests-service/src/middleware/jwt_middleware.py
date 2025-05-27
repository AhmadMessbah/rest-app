from fastapi import Request
from src.services.auth_service import get_current_user

async def jwt_middleware(request: Request, call_next):
    if request.url.path.startswith(("/docs", "/openapi.json", "/metrics")):
        return await call_next(request)
    return await call_next(request)