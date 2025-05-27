# راهنمای سریع Image Requests Service

## مستندات مرجع

- [FastAPI Documentation](https://fastapi.tiangolo.com/)
- [Uvicorn](https://www.uvicorn.org/)
- [MongoDB Python Driver](https://pymongo.readthedocs.io/)
- [Beanie ODM](https://roman-right.github.io/beanie/)
- [Prometheus Monitoring](https://prometheus.io/docs/introduction/overview/)

## نکات کلیدی

- پورت پیش‌فرض: 8000
- مسیر مستندات: `/docs` و `/redoc`
- JWT Secret باید در متغیر محیطی قرار گیرد.
- برای توسعه محلی، از دیتابیس MongoDB لوکال استفاده کنید.

## عیب‌یابی

- خطاهای 401: بررسی JWT و تنظیمات Security
- خطاهای دیتابیس: بررسی اتصال به MongoDB و مقداردهی متغیر MONGODB_URL
- خطاهای OCR: بررسی نصب Tesseract و Pillow
- لاگ‌های خطا: بررسی خروجی کنسول و فایل‌های لاگ

## توسعه و استقرار

- برای اجرای سریع: `uvicorn src.main:app --reload`
- برای تست: `pytest --cov=src tests/`
- برای استقرار در K8s: `kubectl apply -f kubernetes/` 