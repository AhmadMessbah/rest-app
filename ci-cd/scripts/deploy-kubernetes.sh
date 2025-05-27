#!/bin/bash
set -e

TAG=$1

# Update image tags in Kubernetes manifests
for module in api-gateway persons-service image-requests-service web-ui; do
  sed -i "s|image: $module:.*|image: ghcr.io/${{ github.repository }}/$module:$TAG|g" $module/kubernetes/deployment.yml
done

# Apply Kubernetes configurations
kubectl apply -f infrastructure/kubernetes/configmap.yml
kubectl apply -f infrastructure/kubernetes/secrets.yml
kubectl apply -f infrastructure/kubernetes/network-policy.yml
kubectl apply -f infrastructure/kubernetes/hpa.yml
kubectl apply -f infrastructure/kubernetes/postgresql/
kubectl apply -f infrastructure/kubernetes/mongodb/
kubectl apply -f infrastructure/kubernetes/redis/
kubectl apply -f infrastructure/kubernetes/prometheus/
kubectl apply -f infrastructure/kubernetes/grafana/
kubectl apply -f api-gateway/kubernetes/
kubectl apply -f persons-service/kubernetes/
kubectl apply -f image-requests-service/kubernetes/
kubectl apply -f web-ui/kubernetes/

# Notify Slack
chmod +x ci-cd/scripts/notify-slack.sh
ci-cd/scripts/notify-slack.sh "success" "Deployment to $TAG" "${{ github.repository }}"