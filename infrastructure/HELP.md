# راهنمای سریع Infrastructure

## مستندات مرجع

- [Docker Documentation](https://docs.docker.com/)
- [Kubernetes Documentation](https://kubernetes.io/docs/)
- [Prometheus](https://prometheus.io/docs/introduction/overview/)
- [Grafana](https://grafana.com/docs/grafana/latest/)
- [ArgoCD](https://argo-cd.readthedocs.io/en/stable/)
- [ELK Stack](https://www.elastic.co/what-is/elk-stack)

## نکات کلیدی

- مسیر اصلی Docker Compose: `infrastructure/docker/`
- مسیر اصلی Kubernetes: `infrastructure/kubernetes/`
- مانیتورینگ: Prometheus و Grafana
- لاگینگ: ELK Stack (Elasticsearch, Logstash, Kibana)
- GitOps: ArgoCD

## عیب‌یابی

- خطاهای اتصال سرویس‌ها: بررسی NetworkPolicy و Service
- خطاهای دیتابیس: بررسی PVC و Secrets
- مانیتورینگ: بررسی targets در Prometheus و داشبوردهای Grafana
- لاگینگ: بررسی لاگ‌های Filebeat و Kibana
- GitOps: بررسی sync status در ArgoCD

## دستورات سریع

- اجرای Docker Compose:
  ```bash
  cd infrastructure/docker
  docker-compose up --build
  ```
- استقرار در Minikube:
  ```bash
  minikube start --driver=docker
  kubectl apply -f infrastructure/kubernetes/
  ```
- مشاهده سرویس‌ها:
  ```bash
  minikube service list
  ```
- مشاهده وضعیت پادها:
  ```bash
  kubectl get pods -A
  ``` 