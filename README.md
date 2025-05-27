# REST App - Microservices Platform

این پروژه یک پلتفرم مدرن مبتنی بر معماری microservices است که با استفاده از Spring Boot 3.4.6، Java 17، Python 3.12 و React توسعه یافته و برای محیط‌های production و cloud-native بهینه شده است.

## معماری و فناوری‌ها

- **API Gateway**: مدیریت ورودی و امنیت، مبتنی بر Spring Cloud Gateway
- **Persons Service**: مدیریت اطلاعات اشخاص (CRUD)، مبتنی بر Spring Boot و PostgreSQL
- **Image Requests Service**: پردازش تصویر و OCR، مبتنی بر FastAPI و MongoDB
- **Web UI**: رابط کاربری مدرن با React و Tailwind CSS
- **Infrastructure**: استقرار با Docker Compose و Kubernetes (Minikube)، مانیتورینگ (Prometheus/Grafana)، لاگینگ (ELK)، GitOps (ArgoCD)
- **CI/CD**: پیاده‌سازی DevOps با GitHub Actions، SonarQube، Codecov و Trivy

## ساختار پروژه

- `api-gateway/` : دروازه ورودی و مدیریت امنیت
- `persons-service/` : سرویس مدیریت اشخاص
- `image-requests-service/` : سرویس پردازش تصویر و OCR
- `web-ui/` : رابط کاربری وب
- `infrastructure/` : زیرساخت و استقرار (Docker, K8s, Monitoring, Logging)
- `ci-cd/` : اسکریپت‌ها و تنظیمات CI/CD

## نحوه اجرا (محلی)

1. **پیش‌نیازها:**
   - Docker 27+
   - Minikube 1.33+
   - Node.js 20+
   - Python 3.12+
   - Java 17+

2. **اجرای سریع با Docker Compose:**
   ```bash
   cd infrastructure/docker
   docker-compose up --build
   ```

3. **دسترسی به سرویس‌ها:**
   - Web UI: http://localhost:3000
   - API Gateway: http://localhost:8080
   - Prometheus: http://localhost:9090
   - Grafana: http://localhost:3001
   - Kibana: http://localhost:5601

## استقرار در Minikube

1. اجرای Minikube:
   ```bash
   minikube start --driver=docker
   ```
2. ساخت ایمیج‌ها و اعمال تنظیمات:
   ```bash
   eval $(minikube docker-env)
   # build images for all services
   # kubectl apply -f infrastructure/kubernetes/
   ```
3. دسترسی به سرویس‌ها:
   ```bash
   minikube service web-ui
   minikube service grafana
   minikube service kibana
   ```

## توسعه و تست

- هر سرویس README و HELP مخصوص خود را دارد (به پوشه مربوطه مراجعه کنید)
- تست خودکار با GitHub Actions و پوشش کد با Codecov
- تست محلی:
  - Java: `mvn test`
  - Python: `pytest --cov=src tests/`
  - JS: `npm test`

## امنیت و best practiceها

- استفاده از JWT برای احراز هویت
- ذخیره Secrets در محیط امن (K8s Secrets, GitHub Secrets)
- مانیتورینگ و لاگینگ مرکزی
- تست امنیتی با Trivy و SonarQube
- استقرار امن با NetworkPolicy و RBAC
- GitOps با ArgoCD

## مشارکت و توسعه

- هرگونه مشارکت (issue, PR) خوش‌آمد است
- لطفاً قبل از ارسال PR، تست‌ها را اجرا و مستندات را به‌روزرسانی کنید

## لایسنس

[MIT](LICENSE) 