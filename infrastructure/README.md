Infrastructure
Comprehensive infrastructure configurations for deploying the REST App in localhost and Minikube.
Overview
This directory contains configurations for:

Docker Compose: Local development with PostgreSQL, MongoDB, Redis, Prometheus, Grafana, and microservices.
Kubernetes: Production-ready deployments with ConfigMap, Secrets, Network Policies, and Horizontal Pod Autoscaling.
Databases: PostgreSQL (persons-service), MongoDB (image-requests-service), Redis (api-gateway).
Monitoring: Prometheus and Grafana for metrics and dashboards.
Logging: ELK Stack (Elasticsearch, Logstash, Kibana) for centralized logging.
GitOps: ArgoCD for managing Kubernetes deployments.

Prerequisites

Docker: 27.3.1+
Minikube: 1.33.1+
kubectl: 1.29+
Node.js: 20+ (for web-ui)
Python: 3.11+ (for image-requests-service)
Java: 21+ (for api-gateway, persons-service)
ArgoCD CLI: 2.12.6+ (for GitOps)

Local Setup (Docker Compose)

Build and run services:
cd infrastructure/docker
docker-compose up --build


Access services:

Web UI: http://localhost:3000
API Gateway: http://localhost:8080
Prometheus: http://localhost:9090
Grafana: http://localhost:3001 (login: admin/securegrafanapassword)
PostgreSQL: localhost:5432 (user: admin, password: securepassword)
MongoDB: localhost:27017
Redis: localhost:6379
Kibana: http://localhost:5601 (after adding ELK Stack)


Stop services:
docker-compose down



Minikube Setup

Start Minikube:
minikube start --driver=docker


Build Docker images:
eval $(minikube docker-env)
cd api-gateway && docker build -t api-gateway:latest .
cd ../persons-service && docker build -t persons-service:latest .
cd ../image-requests-service && docker build -t image-requests-service:latest .
cd ../web-ui && docker build -t web-ui:latest .


Apply Kubernetes configurations:
kubectl apply -f infrastructure/kubernetes/configmap.yml
kubectl apply -f infrastructure/kubernetes/secrets.yml
kubectl apply -f infrastructure/kubernetes/network-policy.yml
kubectl apply -f infrastructure/kubernetes/hpa.yml
kubectl apply -f infrastructure/kubernetes/postgresql/
kubectl apply -f infrastructure/kubernetes/mongodb/
kubectl apply -f infrastructure/kubernetes/redis/
kubectl apply -f infrastructure/kubernetes/prometheus/
kubectl apply -f infrastructure/kubernetes/grafana/
kubectl apply -f infrastructure/kubernetes/elasticsearch/
kubectl apply -f infrastructure/kubernetes/kibana/
kubectl apply -f infrastructure/kubernetes/logstash/
kubectl apply -f api-gateway/kubernetes/
kubectl apply -f persons-service/kubernetes/
kubectl apply -f image-requests-service/kubernetes/
kubectl apply -f web-ui/kubernetes/


Install ArgoCD (optional for production):
kubectl apply -f infrastructure/kubernetes/argocd/install.yml
kubectl apply -f infrastructure/kubernetes/argocd/application.yml


Access services:
minikube service web-ui
minikube service grafana
minikube service kibana



Best Practices
Security

Secrets: Store sensitive data (JWT_SECRET, database credentials) in Kubernetes Secrets.
Network Policies: Restrict traffic to specific ports (network-policy.yml).
RBAC: Use Role-Based Access Control for Kubernetes (not included here; implement for production).
TLS: Enable HTTPS for api-gateway and web-ui in production (use Ingress with cert-manager).
ELK Security: Enable xpack.security in Elasticsearch for production.

Scalability

HPA: Horizontal Pod Autoscaling enabled for api-gateway, persons-service, and image-requests-service (hpa.yml).
Load Balancing: Use Kubernetes Services with type LoadBalancer for web-ui, grafana, and kibana.
Persistent Storage: PostgreSQL uses PersistentVolumeClaim for data persistence.

Monitoring

Prometheus: Scrapes metrics from api-gateway (/actuator/prometheus), persons-service (/actuator/prometheus), image-requests-service (/metrics), and postgresql (/metrics).
Grafana: Configure dashboards for CPU, memory, and request latency (http://localhost:3001).
Alerting: Add Prometheus Alertmanager for production (not included here).

Logging

ELK Stack: Centralized logging with Elasticsearch, Logstash, and Kibana.
Filebeat: Configure Filebeat in microservices to send logs to Logstash (not included; add for production).

GitOps

ArgoCD: Manages Kubernetes manifests with automated sync (application.yml).
Sync Policy: Automated pruning and self-healing for consistency.

Backup

PostgreSQL: Schedule regular backups using pg_dump:docker exec -t <postgresql-container> pg_dump -U admin personsdb > backup.sql


MongoDB: Use mongodump for backups:docker exec -t <mongodb-container> mongodump --db image_requests_db --out /backup


Redis: Enable AOF persistence in redis.conf for production.
Elasticsearch: Use snapshots for backups (configure for production).

Troubleshooting

PostgreSQL Connection Issues: Verify SPRING_DATASOURCE_URL, username, and password in persons-service.
MongoDB Index Errors: Ensure init-mongo.js runs correctly (check logs: kubectl logs mongodb).
Prometheus Metrics Missing: Confirm metrics paths in prometheus.yml.
Kibana Access: Check Elasticsearch connectivity (http://elasticsearch:9200).
ArgoCD Sync Failures: Verify repoURL and targetRevision in application.yml.
Minikube Access: Use minikube service <service-name> to expose services.

Notes

Ensure JWT_SECRET is consistent across services.
PostgreSQL is initialized with a persons table and name index.
MongoDB has a text index for full-text search in image_requests.
Grafana requires login for security; update admin_password in grafana.ini for production.
ELK Stack requires Filebeat in microservices for full logging (configure for production).
ArgoCD requires a running server (install.yml) and valid credentials.

For advanced configurations (e.g., Ingress, Alertmanager, Terraform), contact the infrastructure team.
