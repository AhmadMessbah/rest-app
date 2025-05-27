CI/CD
Advanced CI/CD pipelines for the REST App using GitHub Actions.
Overview
This directory contains configurations for:

Continuous Integration: Build, test, security scans, and code quality analysis for all modules.
Continuous Deployment: Deploy to Minikube (develop) and production (main) with manual approval and ArgoCD.
Dependency Management: Automated updates with Dependabot.
Reusable Workflows: Standardized testing for Java modules.
Composite Actions: Security scanning with Trivy.
Code Quality: SonarQube for static code analysis.
Monitoring: Coverage reports with Codecov, notifications via Slack.

Pipelines

CI Pipeline (.github/workflows/ci.yml)

Triggers: Push/PR to main or develop.
Actions:
Test api-gateway and persons-service using reusable workflow (test-java.yml).
Test image-requests-service (Pytest) and web-ui (Vitest).
Cache dependencies for speed.
Upload coverage to Codecov.
Scan dependencies with Trivy (via security-scan action).
Analyze code quality with SonarQube.
Notify Slack on success/failure.




CD to Minikube (.github/workflows/cd-minikube.yml)

Triggers: Push to develop.
Actions:
Build and push Docker images to GHCR.
Scan images with Trivy.
Deploy to Minikube.
Verify deployment status.
Notify Slack.




CD to Production (.github/workflows/cd-production.yml)

Triggers: Push to main.
Actions:
Build and push Docker images to GHCR.
Scan images with Trivy.
Manual approval by repository owner.
Deploy to production cluster.
Trigger ArgoCD sync for GitOps.
Verify deployment status.
Notify Slack.




Dependabot (.github/workflows/dependabot.yml)

Weekly updates for Maven, Pip, NPM, and Docker dependencies.
Maximum 5 open PRs per ecosystem.



Reusable Components

Test Java Workflow (.github/workflows/test-java.yml)

Reusable workflow for testing Java modules (api-gateway, persons-service).
Includes Maven build, JaCoCo coverage, and caching.


Security Scan Action (.github/actions/security-scan/action.yml)

Composite action for scanning dependencies and filesystem with Trivy.



Setup

GitHub Secrets

GHCR_TOKEN: Personal Access Token for pushing to GHCR (scope: write:packages).
CODECOV_TOKEN: Token for Codecov (from https://codecov.io).
SLACK_WEBHOOK_URL: Webhook URL for Slack notifications.
JWT_SECRET: Secret key for JWT authentication.
POSTGRES_USER: PostgreSQL username (e.g., admin).
POSTGRES_PASSWORD: PostgreSQL password (e.g., securepassword).
KUBE_CONFIG: Base64-encoded kubeconfig for production cluster.
SONAR_TOKEN: Token for SonarQube (from SonarQube dashboard).
SONAR_HOST_URL: URL of SonarQube server (e.g., https://sonar.example.com).
ARGOCD_TOKEN: Token for ArgoCD (from ArgoCD server).


Repository Configuration

Enable Dependabot in GitHub Settings > Security.
Set up a production environment in GitHub Settings > Environments with required approvers.


SonarQube Setup

Deploy SonarQube server (e.g., via Docker: docker run -d -p 9000:9000 sonarqube:10.6-community).
Generate SONAR_TOKEN and set SONAR_HOST_URL in GitHub Secrets.


ArgoCD Setup

Install ArgoCD in production cluster (infrastructure/kubernetes/argocd/install.yml).
Configure application (infrastructure/kubernetes/argocd/application.yml).
Generate ARGOCD_TOKEN and set in GitHub Secrets.


Local Testing

Install act for local workflow testing:curl https://raw.githubusercontent.com/nektos/act/master/install.sh | sudo bash
act -W .github/workflows/ci.yml





Best Practices
Security

Secrets: Store sensitive data in GitHub Secrets, never in code.
Trivy: Scan dependencies and Docker images for HIGH/CRITICAL vulnerabilities.
Manual Approval: Require approval for production deployments.
RBAC: Use GitHub Teams for access control to production environment.
SonarQube: Enforce code quality gates to catch vulnerabilities.

Performance

Caching: Cache Maven, Pip, and NPM dependencies to reduce build time.
Parallel Jobs: Run tests for each module in parallel.
Minikube: Use lightweight Minikube for CI/CD testing.

Reliability

Coverage: Aim for >80% test coverage (enforced via Codecov).
Rollback: Use kubectl rollout undo for failed deployments.
Notifications: Immediate Slack alerts for pipeline status.
SonarQube: Monitor technical debt and code smells.

Scalability

HPA: Kubernetes Horizontal Pod Autoscaling enabled (infrastructure/kubernetes/hpa.yml).
Multi-Environment: Separate workflows for develop (Minikube) and main (production).
Reusable Workflows: Reduce duplication with test-java.yml and security-scan action.
ArgoCD: Automated sync for Kubernetes manifests.

Troubleshooting

Build Failures: Check dependency versions or test logs in GitHub Actions.
Docker Push Errors: Verify GHCR_TOKEN permissions.
Minikube Issues: Ensure sufficient resources (4GB RAM, 2 CPUs).
Production Deployment: Confirm KUBE_CONFIG and cluster access.
SonarQube Errors: Verify SONAR_TOKEN and SONAR_HOST_URL.
ArgoCD Sync Failures: Check repoURL and ARGOCD_TOKEN.

Notes

Ensure JWT_SECRET is consistent across all services.
PostgreSQL requires a persons table with name index (infrastructure/docker/postgresql/init.sql).
MongoDB has a text index for image_requests (infrastructure/docker/mongodb/init-mongo.js).
ELK Stack requires Filebeat in microservices for full logging.
Update SLACK_WEBHOOK_URL, SONAR_TOKEN, and ARGOCD_TOKEN in GitHub Secrets.

For advanced setups (e.g., Terraform, Canary Deployments), contact the DevOps team.
