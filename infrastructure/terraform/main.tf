provider "kubernetes" {
  config_path = "~/.kube/config"
}

resource "kubernetes_namespace" "app" {
  metadata {
    name = "default"
  }
}

resource "kubernetes_deployment" "postgresql" {
  metadata {
    name      = "postgresql"
    namespace = kubernetes_namespace.app.metadata[0].name
  }
  spec {
    replicas = 1
    selector {
      match_labels = {
        app = "postgresql"
      }
    }
    template {
      metadata {
        labels = {
          app = "postgresql"
        }
      }
      spec {
        container {
          image = "postgres:16"
          name  = "postgresql"
          port {
            container_port = 5432
          }
          env {
            name  = "POSTGRES_DB"
            value = "personsdb"
          }
          env {
            name = "POSTGRES_USER"
            value_from {
              secret_key_ref {
                name = "app-secrets"
                key  = "postgres-user"
              }
            }
          }
          env {
            name = "POSTGRES_PASSWORD"
            value_from {
              secret_key_ref {
                name = "app-secrets"
                key  = "postgres-password"
              }
            }
          }
        }
      }
    }
  }
}