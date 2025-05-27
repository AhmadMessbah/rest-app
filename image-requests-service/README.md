# Image Requests Service

این سرویس برای آپلود تصویر، استخراج متن (OCR) و جستجوی متنی با FastAPI و Python 3.12 توسعه یافته است. داده‌ها در MongoDB ذخیره می‌شوند و احراز هویت JWT پیاده‌سازی شده است.

## ساختار پروژه

- **src/**: کد اصلی FastAPI و منطق سرویس
- **tests/**: تست‌های واحد و یکپارچه
- **openapi.yml**: مستندات کامل API
- **Dockerfile** و **docker-compose.yml**: اجرای سرویس در محیط Docker و K8s
- **kubernetes/**: فایل‌های استقرار در K8s

## وابستگی‌ها

- FastAPI
- Uvicorn
- Pymongo/Beanie
- pytesseract, pillow (OCR)
- python-jose, passlib (JWT)
- prometheus-fastapi-instrumentator (مانیتورینگ)

## نحوه اجرا

```bash
pip install -r requirements.txt
uvicorn src.main:app --reload
```
یا با Docker:
```bash
docker build -t image-requests-service:latest .
docker run -p 8000:8000 image-requests-service:latest
```

## متغیرهای محیطی

- `MONGODB_URL`: آدرس دیتابیس MongoDB
- `JWT_SECRET`: کلید JWT

## مستندات API

مستندات کامل OpenAPI در مسیر `/docs` و `/redoc` در دسترس است. نمونه API:
- POST `/api/image-requests/` (آپلود تصویر و OCR)
- GET `/api/image-requests/` (لیست تصاویر)
- GET `/api/image-requests/{id}` (جزئیات تصویر)
- GET `/api/image-requests/search/?query=متن` (جستجوی متنی)

## Best Practices

- استفاده از JWT برای امنیت
- مانیتورینگ با Prometheus
- تست با pytest و httpx
- لاگینگ مرکزی با Filebeat
- استقرار امن در K8s با Secrets و NetworkPolicy

## توسعه و تست

برای تست:
```bash
pytest --cov=src tests/
```

برای استقرار در Minikube:
```bash
kubectl apply -f kubernetes/
``` 