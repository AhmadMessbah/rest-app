# Web UI

React-based web interface for interacting with the REST App microservices.

## Setup

1. Install dependencies:
   ```bash
   npm install
   ```

2. Run locally:
   ```bash
   npm run dev
   ```

3. Build for production:
   ```bash
   npm run build
   ```

## Environment Variables

- `VITE_API_URL`: Base URL for the API Gateway (default: `http://localhost:8080/api`)

## Deployment

Build Docker image:
```bash
docker build -t web-ui:latest .
```

Deploy to Minikube:
```bash
kubectl apply -f kubernetes/deployment.yml
kubectl apply -f kubernetes/service.yml
```

Access the UI via Minikube:
```bash
minikube service web-ui
```