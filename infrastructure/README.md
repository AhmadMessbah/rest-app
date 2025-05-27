# Infrastructure

این پوشه شامل تمامی تنظیمات و ابزارهای زیرساختی برای استقرار، مانیتورینگ، لاگینگ و امنیت پروژه REST App است.

## اجزای اصلی

- **docker/**: اجرای محلی با Docker Compose (پایگاه داده‌ها، سرویس‌ها، مانیتورینگ، لاگینگ)
- **kubernetes/**: استقرار production-ready با K8s (ConfigMap, Secrets, NetworkPolicy, HPA)
- **monitoring/**: تنظیمات Prometheus و Grafana
- **terraform/**: (در صورت وجود) مدیریت زیرساخت ابری

## سناریوهای استقرار

### اجرای محلی (Docker Compose)
```bash
cd infrastructure/docker
docker-compose up --build
```

### استقرار در Minikube (Kubernetes)
```bash
minikube start --driver=docker
eval $(minikube docker-env)
kubectl apply -f infrastructure/kubernetes/
```

### GitOps با ArgoCD
```bash
kubectl apply -f infrastructure/kubernetes/argocd/install.yml
kubectl apply -f infrastructure/kubernetes/argocd/application.yml
```

## امنیت و best practiceها

- استفاده از Secrets برای داده‌های حساس
- محدودسازی دسترسی با NetworkPolicy و RBAC
- فعال‌سازی TLS و HTTPS برای سرویس‌های حیاتی
- مانیتورینگ و هشداردهی با Prometheus و Alertmanager
- لاگینگ مرکزی با ELK و Filebeat
- پشتیبان‌گیری منظم دیتابیس‌ها

## عیب‌یابی

- بررسی لاگ سرویس‌ها: `kubectl logs <pod>`
- بررسی وضعیت پادها: `kubectl get pods -A`
- بررسی داشبورد Grafana و Kibana
- بررسی sync status در ArgoCD

## منابع بیشتر

- [Kubernetes Docs](https://kubernetes.io/docs/)
- [Prometheus Docs](https://prometheus.io/docs/)
- [Grafana Docs](https://grafana.com/docs/)
- [ArgoCD Docs](https://argo-cd.readthedocs.io/en/stable/)
