# 🐳 MoveInSync Backend - Microservices

Dockerized Spring Boot microservices for the MoveInSync Unified Billing Platform.

---

## 🚀 Quick Start with Docker

### Prerequisites
- **Docker** 20.10+ ([Install Docker](https://docs.docker.com/get-docker/))
- **Docker Compose** 2.0+ (included with Docker Desktop)
- **8GB RAM** minimum for running all services

### Start All Services

```bash
# Build and start all services in detached mode (recommended)
docker-compose up --build -d

# Start in foreground (see logs in real-time)
docker-compose up --build

# Start without rebuilding (after first build)
docker-compose up -d

# View logs
docker-compose logs -f

# View logs for specific service
docker-compose logs -f auth-service
```

### Stop Services

```bash
# Stop all services (keeps data)
docker-compose down

# Stop and remove volumes (removes all data)
docker-compose down -v

# Stop and remove images (complete cleanup)
docker-compose down -v --rmi all
```

---

## 📊 Services & Ports

All services are accessible on `localhost`:

| Service | Port | URL | Health Check |
|---------|------|-----|--------------|
| **Auth Service** | 4005 | http://localhost:4005 | http://localhost:4005/actuator/health |
| **Client Service** | 4010 | http://localhost:4010 | http://localhost:4010/actuator/health |
| **Vendor Service** | 4015 | http://localhost:4015 | http://localhost:4015/actuator/health |
| **Trip Service** | 4020 | http://localhost:4020 | http://localhost:4020/actuator/health |
| **Billing Service** | 4025 | http://localhost:4025 | http://localhost:4025/actuator/health |
| **Employee Service** | 4035 | http://localhost:4035 | http://localhost:4035/actuator/health |
| **MySQL Database** | 3306 | localhost:3306 | - |

---

## 🏗️ Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                     Docker Network                          │
│                  (moveinsync-network)                       │
│                                                             │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐    │
│  │ Auth Service │  │Client Service│  │Vendor Service│    │
│  │   :4005      │  │   :4010      │  │   :4015      │    │
│  └──────┬───────┘  └──────┬───────┘  └──────┬───────┘    │
│         │                 │                  │            │
│  ┌──────┴───────┐  ┌──────┴───────┐  ┌──────┴───────┐    │
│  │ Trip Service │  │Billing Svc   │  │Analytics Svc │    │
│  │   :4020      │  │   :4025      │  │   :4030      │    │
│  └──────┬───────┘  └──────┬───────┘  └──────┬───────┘    │
│         │                 │                  │            │
│  ┌──────┴────────────────┴──────────────────┴────┐       │
│  │            Employee Service :4035              │       │
│  └──────┬─────────────────────────────────────────┘       │
│         │                                                 │
│  ┌──────┴────────────────────────────────────────┐       │
│  │          MySQL Database :3306                 │       │
│  │  (unified_billing_* databases)                │       │
│  └───────────────────────────────────────────────┘       │
└─────────────────────────────────────────────────────────────┘
```

---

## 📦 Service Details

### 1. **Auth Service** (:4005)
- JWT authentication and user management
- User roles: ADMIN, CLIENT, VENDOR, EMPLOYEE, FINANCE_TEAM
- Database: `unified_billing_auth`

### 2. **Client Service** (:4010)
- Corporate client management
- Tenant isolation
- Database: `unified_billing_clients`

### 3. **Vendor Service** (:4015)
- Transportation vendor management (Ola, Uber, etc.)
- Vendor profiles and ratings
- Database: `unified_billing_vendors`

### 4. **Trip Service** (:4020)
- Trip tracking and management
- Billing model configuration
- Database: `unified_billing_trips`

### 5. **Billing Service** (:4025)
- Billing calculations (TRIP, PACKAGE, HYBRID models)
- Invoice generation
- GST calculation
- Report generation (Client, Vendor, Employee, Consolidated reports)
- Database: `unified_billing_billing`

### 6. **Employee Service** (:4035)
- Employee management
- Incentive tracking
- Database: `unified_billing_employees`

---

## 🔧 Docker Commands

### Build Services

```bash
# Build all services
docker-compose build

# Build specific service
docker-compose build auth-service

# Build without cache (clean build)
docker-compose build --no-cache
```

### Service Management

```bash
# Start specific service
docker-compose up auth-service

# Restart service
docker-compose restart auth-service

# Stop specific service
docker-compose stop auth-service

# View running containers
docker-compose ps

# View all containers (including stopped)
docker-compose ps -a
```

### Logs & Debugging

```bash
# View all logs
docker-compose logs

# Follow logs (real-time)
docker-compose logs -f

# Last 100 lines of logs
docker-compose logs --tail=100

# Logs for specific service
docker-compose logs auth-service

# Execute command in running container
docker-compose exec auth-service sh

# View service environment variables
docker-compose exec auth-service env
```

### Database Operations

```bash
# Connect to MySQL
docker-compose exec mysql mysql -u root -pQwerty@cs12345

# Backup database
docker-compose exec mysql mysqldump -u root -pQwerty@cs12345 --all-databases > backup.sql

# Restore database
docker-compose exec -T mysql mysql -u root -pQwerty@cs12345 < backup.sql

# View databases
docker-compose exec mysql mysql -u root -pQwerty@cs12345 -e "SHOW DATABASES;"
```

### Cleanup

```bash
# Remove all containers
docker-compose down

# Remove containers and volumes
docker-compose down -v

# Remove containers, volumes, and images
docker-compose down -v --rmi all

# Clean up Docker system (careful!)
docker system prune -a
```

---

## 🧪 Testing

### Health Checks

```bash
# Check all services are healthy
for port in 4005 4010 4015 4020 4025 4035; do
  echo "Testing port $port..."
  curl -s http://localhost:$port/actuator/health | grep -q "UP" && echo "✅ Port $port is UP" || echo "❌ Port $port is DOWN"
done
```

### API Testing

```bash
# Login and get JWT token
curl -X POST http://localhost:4005/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@moveinsync.com","password":"password"}'

# Get clients (with token)
curl -X GET http://localhost:4010/clients \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

---

## 🔐 Environment Variables

Default configuration (can be overridden in `docker-compose.yml`):

```yaml
SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/[database]?...
SPRING_DATASOURCE_USERNAME: root
SPRING_DATASOURCE_PASSWORD: Qwerty@cs12345
```

**⚠️ For production:** Change these credentials!

---

## 📁 Project Structure

```
backend/
├── docker-compose.yml          # Main orchestration file
├── .dockerignore              # Files to exclude from Docker
├── auth-service/
│   ├── Dockerfile
│   ├── pom.xml
│   └── src/
├── client-service/
│   ├── Dockerfile
│   ├── pom.xml
│   └── src/
├── vendor-service/
│   ├── Dockerfile
│   ├── pom.xml
│   └── src/
├── trip-service/
│   ├── Dockerfile
│   ├── pom.xml
│   └── src/
├── billing-service/
│   ├── Dockerfile
│   ├── pom.xml
│   └── src/
├── analytics-service/
│   ├── Dockerfile
│   ├── pom.xml
│   └── src/
└── employee-service/
    ├── Dockerfile
    ├── pom.xml
    └── src/
```

---

## 🐛 Troubleshooting

### Port Already in Use

```bash
# Find process using port (Windows)
netstat -ano | findstr :4005
taskkill /PID <process_id> /F

# Find process using port (Linux/Mac)
lsof -i :4005
kill -9 <process_id>
```

### Service Won't Start

```bash
# Check logs
docker-compose logs service-name

# Check if MySQL is healthy
docker-compose ps mysql

# Restart MySQL
docker-compose restart mysql

# Rebuild service
docker-compose up --build service-name
```

### Database Connection Issues

```bash
# Test MySQL connection
docker-compose exec mysql mysql -u root -pQwerty@cs12345 -e "SELECT 1;"

# Check MySQL logs
docker-compose logs mysql

# Recreate MySQL with fresh data
docker-compose down -v
docker-compose up mysql
```

### Out of Memory

```bash
# Check Docker resources
docker stats

# Increase Docker memory (Docker Desktop > Settings > Resources)
# Recommended: 8GB RAM minimum

# Stop unused containers
docker stop $(docker ps -aq)
```

---

## 🚀 Production Deployment

### Best Practices

1. **Change default credentials** in `docker-compose.yml`
2. **Use environment files** (`.env`) for secrets
3. **Enable HTTPS/SSL** with reverse proxy (nginx, traefik)
4. **Set up monitoring** (Prometheus, Grafana)
5. **Configure log aggregation** (ELK stack)
6. **Use managed database** instead of Docker MySQL
7. **Set resource limits** for containers
8. **Use Docker secrets** for sensitive data

### Production docker-compose.yml Example

```yaml
services:
  auth-service:
    deploy:
      resources:
        limits:
          cpus: '1.0'
          memory: 512M
        reservations:
          cpus: '0.5'
          memory: 256M
    environment:
      - SPRING_PROFILES_ACTIVE=prod
      - SPRING_DATASOURCE_PASSWORD=${DB_PASSWORD}
```

---

## 📚 Additional Resources

- [Docker Documentation](https://docs.docker.com/)
- [Docker Compose Reference](https://docs.docker.com/compose/compose-file/)
- [Spring Boot Docker Guide](https://spring.io/guides/topicals/spring-boot-docker/)

---

## 📄 License

Educational project for MoveInSync case study.

---

**Built with 🐳 Docker + ☕ Spring Boot**
