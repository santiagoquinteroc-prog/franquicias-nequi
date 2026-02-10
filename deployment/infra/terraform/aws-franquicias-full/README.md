# AWS Infrastructure – Franquicias API (API Gateway First)

Este stack de Terraform provisiona la infraestructura completa en AWS para desplegar la **Franquicias API**
usando **ECS Fargate**, **RDS PostgreSQL**, **ECR** y **observabilidad con CloudWatch**.

✅ **Punto principal (entrada pública): API Gateway**  
API Gateway expone el endpoint público y enruta el tráfico hacia un **Application Load Balancer (ALB)**,
que a su vez balancea hacia el servicio en **ECS**.

---

## Arquitectura (High Level)

**Cliente → API Gateway → ALB → ECS Fargate → RDS PostgreSQL**

Componentes:

- **Amazon API Gateway (HTTP API)**
    - Entrada pública principal
    - Permite control de rutas, throttling, auth (si lo habilitas), CORS centralizado
- **Application Load Balancer (ALB)**
    - Backend HTTP para ECS
    - Health checks y balanceo hacia tasks
- **Amazon ECS Fargate**
    - Ejecuta el contenedor de la API (Spring WebFlux + R2DBC)
- **Amazon ECR**
    - Repositorio de imágenes Docker
- **Amazon RDS PostgreSQL**
    - Base de datos administrada
- **CloudWatch Logs**
    - Logs centralizados del contenedor (ECS)
- **Security Groups**
    - Segmentación de tráfico entre API Gateway/ALB/ECS/RDS

---

## Requisitos

- AWS Account con permisos para:
    - API Gateway (HTTP API)
    - ECS (cluster, service, task definition)
    - ECR
    - RDS
    - ALB
    - IAM
    - CloudWatch Logs
    - VPC/Subnets/Security Groups
- AWS CLI configurado:
  ```bash
  aws configure


# Pasos de despliegue con Terraform (API Gateway First)

## Ubicación del stack

Desde la raíz del proyecto:

```bash
cd deployment/infra/terraform/aws-franquicias-full
```

---

## 1. Inicializar Terraform

```bash
terraform init
```

---

## 2. Validar cambios (plan)

```bash
terraform plan
```

---

## 3. Crear la infraestructura

```bash
terraform apply
```

> ⚠️ **Nota:** Si aún no existe una imagen en ECR, es normal que el servicio ECS no quede healthy hasta hacer el push.

---

## Outputs importantes

Después de finalizar `terraform apply`:

```bash
terraform output
```

**Outputs más utilizados:**

- `api_gateway_url` → Entrada pública principal
- `swagger_url`
- `ecr_repository_url`
- `rds_endpoint`
- `alb_dns_name` (interno, detrás del API Gateway)

---

## Despliegue de la aplicación (ECR → ECS)

### Paso 1 – Infraestructura creada

Asegúrate de haber ejecutado:

```bash
terraform apply
```

### Paso 2 – Build y push de imagen Docker

#### Obtener URL del repositorio ECR

```bash
terraform output -raw ecr_repository_url
```

#### Login en ECR

```bash
aws ecr get-login-password --region <REGION> \
  | docker login --username AWS --password-stdin <ECR_URL>
```

#### Build de la imagen

```bash
docker build -t <ECR_URL>:<TAG> -f deployment/Dockerfile .
```

**Ejemplo:**

```bash
docker build -t 123456789012.dkr.ecr.us-east-2.amazonaws.com/red-franquicias:latest -f deployment/Dockerfile .
```

#### Push de la imagen

```bash
docker push <ECR_URL>:<TAG>
```

### Paso 3 – Forzar redeploy en ECS

**Si usas el mismo tag (`latest`):**

```bash
aws ecs update-service \
  --cluster <CLUSTER_NAME> \
  --service <SERVICE_NAME> \
  --force-new-deployment \
  --region <REGION>
```

**Alternativa (si cambias `image_tag` en `terraform.tfvars`):**

```bash
terraform apply
```

---

## Acceso a la API

### URL base (API Gateway)

```bash
terraform output -raw api_gateway_url
```

### Swagger UI

```
<API_GATEWAY_URL>/swagger-ui.html
```

### Health Check

```
<API_GATEWAY_URL>/actuator/health
```

---

## Logs

Los logs del contenedor se encuentran en **CloudWatch Logs**:

```
/ecs/<SERVICE_NAME>
```

---

## Destruir infraestructura

> ⚠️ **Elimina todos los recursos (incluye base de datos):**

```bash
terraform destroy
```

---
