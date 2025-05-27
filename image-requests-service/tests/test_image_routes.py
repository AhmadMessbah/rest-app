import pytest
from fastapi.testclient import TestClient
from src.main import app

client = TestClient(app)

@pytest.mark.asyncio
async def test_create_image_request():
    response = client.post(
        "/api/image-requests/",
        files={"file": ("test.jpg", b"fake-image-data", "image/jpeg")},
        headers={"Authorization": "Bearer valid-token"}
    )
    assert response.status_code == 201
    assert "extracted_text" in response.json()

@pytest.mark.asyncio
async def test_get_image_request():
    response = client.get("/api/image-requests/123", headers={"Authorization": "Bearer valid-token"})
    assert response.status_code == 404  # Since no real data exists


# curl -X POST "http://localhost:8000/auth/token" -d '{"user_id": "test_user"}'
# curl -X POST "http://localhost:8000/" -H "Authorization: Bearer <your_token>" -F "file=@/path/to/image.jpg"
# curl -X GET "http://localhost:8000/<image_request_id>" -H "Authorization: Bearer <your_token>"