# Deployment Setup Documentation

## Changes Made

### 1. Docker Compose Configuration Updates

**File:** `docker-compose.yaml`

#### Environment File Path Updates
All services now use environment files from the `/home/ubuntu/quickhammer-configs` directory with hyphenated folder names:

- ✅ `/home/ubuntu/quickhammer-configs/auth-service/.env`
- ✅ `/home/ubuntu/quickhammer-configs/bid-service/.env`
- ✅ `/home/ubuntu/quickhammer-configs/job-service/.env`
- ✅ `/home/ubuntu/quickhammer-configs/notification-service/.env`
- ✅ `/home/ubuntu/quickhammer-configs/payment-service/.env`
- ✅ `/home/ubuntu/quickhammer-configs/user-service/.env`
- ✅ `/home/ubuntu/quickhammer-configs/api-gateway/.env`

#### Service Configuration
All microservices are configured to:
- Pull the latest Docker images from Docker Hub (`thetharz/quickhammer-*`)
- Use proper port mappings
- Reference correct database dependencies
- Auto-restart unless stopped

### 2. CI/CD Workflow Updates

#### Build Workflows Fixed
Fixed naming inconsistencies in build workflows:

- ✅ `api-gateway.yaml` - Changed from "Build & Push User Service" to "Build & Push API Gateway"
- ✅ `bid-service.yaml` - Changed from "Build & Push BId Service" to "Build & Push Bid Service"
- ✅ `eureka-server.yaml` - Changed from "Build & Push BId Service" to "Build & Push Eureka Server"
- ✅ `payment-service.yaml` - Changed from "Build & Push User Service" to "Build & Push Payment Service"

#### New Deployment Workflows Created

Created automated CD pipelines that trigger after successful builds:

1. **deploy-auth-service.yaml**
   - Triggers on: "Build & Push Auth Service" completion
   - Deploys: auth-service

2. **deploy-api-gateway.yaml**
   - Triggers on: "Build & Push API Gateway" completion
   - Deploys: api-gateway

3. **deploy-bid-service.yaml**
   - Triggers on: "Build & Push Bid Service" completion
   - Deploys: bid-service

4. **deploy-job-service.yaml**
   - Triggers on: "Build & Push Job Service" completion
   - Deploys: job-service

5. **deploy-notification-service.yaml**
   - Triggers on: "Build & Push Notification Service" completion
   - Deploys: notification-service

6. **deploy-payment-service.yaml**
   - Triggers on: "Build & Push Payment Service" completion
   - Deploys: payment-service

7. **deploy-user-service.yaml**
   - Triggers on: "Build & Push User Service" completion
   - Deploys: user-service

8. **deploy-eureka-server.yaml**
   - Triggers on: "Build & Push Eureka Server" completion
   - Deploys: eureka-server

### 3. Deployment Workflow Features

Each deployment workflow:
1. ✅ Triggers automatically when the corresponding build workflow succeeds
2. ✅ Pulls latest configs from `/home/ubuntu/quickhammer-configs`
3. ✅ Pulls latest Docker image for the specific service
4. ✅ Restarts only the affected service (zero-downtime deployment)
5. ✅ Cleans up unused Docker images to save disk space

### 4. Required GitHub Secrets

Ensure these secrets are configured in your GitHub repository:

- `SERVER_HOST` - Production server IP address
- `SERVER_USER` - SSH username (e.g., ubuntu)
- `SERVER_SSH_KEY` - SSH private key for authentication
- `DOCKERHUB_USERNAME` - Docker Hub username (thetharz)
- `DOCKERHUB_TOKEN` - Docker Hub access token

## Deployment Flow

```
Code Push to main branch
    ↓
Build Workflow Triggers (e.g., Build & Push Auth Service)
    ↓
Docker Image Built & Pushed to Docker Hub
    ↓
Deployment Workflow Triggers (e.g., Deploy Auth Service)
    ↓
SSH to Production Server
    ↓
Pull Latest Configs
    ↓
Pull Latest Docker Image
    ↓
Restart Service
    ↓
Cleanup
```

## Production Server Setup

### Prerequisites on Production Server

1. **Directory Structure:**
   ```bash
   /home/ubuntu/
   ├── quickhammer/           # Docker compose location
   │   └── docker-compose.yaml
   └── quickhammer-configs/   # Environment configs
       ├── auth-service/.env
       ├── bid-service/.env
       ├── job-service/.env
       ├── notification-service/.env
       ├── payment-service/.env
       ├── user-service/.env
       └── api-gateway/.env
   ```

2. **Required Software:**
   - Docker
   - Docker Compose
   - Git

3. **Initial Setup Commands:**
   ```bash
   # Clone configs repository
   cd /home/ubuntu
   git clone <quickhammer-configs-repo-url> quickhammer-configs
   
   # Create quickhammer directory and copy docker-compose.yaml
   mkdir -p /home/ubuntu/quickhammer
   cd /home/ubuntu/quickhammer
   # Copy your docker-compose.yaml here
   
   # Start all services
   docker compose up -d
   ```

## Port Mappings

| Service | Internal Port | External Port |
|---------|--------------|---------------|
| API Gateway | 5000 | 5000 |
| Auth Service | 8080 | 8080 |
| Bid Service | 8081 | 8081 |
| Job Service | 8082 | 8082 |
| Notification Service | 8083 | 8083 |
| Payment Service | 8084 | 8084 |
| User Service | 8085 | 8085 |
| Eureka Server | 8761 | 8761 |
| Kafka | 9092 | 9092 |
| Redis | 6379 | 6379 |
| Auth DB | 5432 | 5432 |
| Bid DB | 5432 | 5433 |
| Job DB | 5432 | 5434 |
| Notification DB | 5432 | 5435 |
| Payment DB | 5432 | 5436 |
| User DB | 5432 | 5437 |
| Zookeeper | 2181 | 2181 |

## Manual Deployment Commands

If you need to manually deploy:

```bash
# SSH to production server
ssh ubuntu@<server-ip>

# Pull latest configs
cd /home/ubuntu/quickhammer-configs
git pull

# Pull and restart specific service
cd /home/ubuntu/quickhammer
docker compose pull auth-service
docker compose up -d auth-service

# Or restart all services
docker compose pull
docker compose up -d

# View logs
docker compose logs -f auth-service

# Cleanup
docker image prune -f
```

## Testing Deployments

After deployment, verify services are running:

```bash
# Check all containers
docker compose ps

# Check specific service logs
docker compose logs -f auth-service

# Check service health
curl http://localhost:8761  # Eureka dashboard
curl http://localhost:5000  # API Gateway
```

## Rollback Procedure

If a deployment fails:

1. SSH to production server
2. Pull the previous working image tag (if tagged)
3. Restart the service
4. Check logs

```bash
cd /home/ubuntu/quickhammer
docker compose down auth-service
docker compose up -d auth-service
docker compose logs -f auth-service
```

## Notes

- Each service deployment is independent and won't affect other services
- Database changes/migrations should be handled carefully
- Monitor logs after each deployment
- The deployment uses the `latest` tag - consider using versioned tags for better control
- All services use the `quickhammer-network` bridge network for internal communication

