# CI/CD

این پوشه شامل تنظیمات و اسکریپت‌های CI/CD برای اتوماسیون build، تست، امنیت و استقرار پروژه REST App است.

## اجزای اصلی

- **.github/workflows/**: تعریف workflowهای GitHub Actions برای build، تست، پوشش کد، اسکن امنیتی و استقرار
- **scripts/**: اسکریپت‌های کمکی برای build و استقرار
- **github-actions/**: اکشن‌های سفارشی (مانند اسکن امنیتی)

## سناریوهای CI/CD

- **CI Pipeline**: اجرای خودکار build و تست برای هر PR و push
- **CD to Minikube**: استقرار خودکار روی Minikube برای شاخه develop
- **CD to Production**: استقرار امن و تاییدشده روی کلاستر production برای شاخه main
- **Dependabot**: به‌روزرسانی خودکار وابستگی‌ها
- **Code Quality**: بررسی کیفیت کد با SonarQube و پوشش کد با Codecov
- **Security Scan**: اسکن آسیب‌پذیری با Trivy
- **GitOps**: استقرار و همگام‌سازی با ArgoCD

## امنیت و best practiceها

- ذخیره Secrets در GitHub Secrets
- فعال‌سازی manual approval برای استقرار production
- اسکن امنیتی مداوم
- مانیتورینگ pipelineها و هشداردهی Slack
- استفاده از workflowهای reusable برای کاهش تکرار

## عیب‌یابی

- بررسی لاگ‌های GitHub Actions
- بررسی وضعیت استقرار در ArgoCD
- بررسی پوشش کد و کیفیت در SonarQube و Codecov

## منابع بیشتر

- [GitHub Actions Docs](https://docs.github.com/en/actions)
- [SonarQube Docs](https://docs.sonarqube.org/latest/)
- [Codecov Docs](https://docs.codecov.com/docs)
- [Trivy Docs](https://aquasecurity.github.io/trivy/v0.18.3/)
- [ArgoCD Docs](https://argo-cd.readthedocs.io/en/stable/)
