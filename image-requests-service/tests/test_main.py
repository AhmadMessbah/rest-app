import pytest
from fastapi.testclient import TestClient
from src.main import app

client = TestClient(app)

def test_create_request():
    response = client.post("/image-requests", json={"description": "Test image"})
    assert response.status_code == 200
    assert response.json()["description"] == "Test image"
    assert response.json()["status"] == "pending"

def test_get_request_not_found():
    response = client.get("/image-requests/nonexistent")
    assert response.status_code == 404
    assert response.json()["detail"] == "Request not found"

def test_health_check():
    response = client.get("/health")
    assert response.status_code == 200
    assert response.json()["status"] == "healthy"