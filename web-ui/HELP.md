# راهنمای سریع Web UI

## مستندات مرجع

- [React Documentation](https://react.dev/)
- [Vite](https://vitejs.dev/)
- [Tailwind CSS](https://tailwindcss.com/)

## نکات کلیدی

- پورت پیش‌فرض توسعه: 3000
- متغیر محیطی: `VITE_API_URL` (آدرس API Gateway)
- برای توسعه محلی، از دستور `npm run dev` استفاده کنید.

## عیب‌یابی

- خطاهای اتصال: بررسی مقداردهی صحیح VITE_API_URL
- خطاهای build: بررسی نسخه Node.js و نصب وابستگی‌ها
- مشکلات استایل: بررسی پیکربندی Tailwind و index.css

## توسعه و استقرار

- build برای production: `npm run build`
- اجرای تست (در صورت وجود): `npm test`
- استقرار در K8s: `kubectl apply -f kubernetes/` 