# 🚀 MoveInSync - Unified Billing & Reporting Platform

A complete **microservices-based** transportation management and billing platform for corporate employee transport services. Built with **Spring Boot** (backend) and **React + Vite** (frontend).

---

## 📋 Table of Contents

- [Overview](#overview)
- [Architecture](#architecture)
- [Quick Start](#quick-start)
- [Services & Ports](#services--ports)
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Database Setup](#database-setup)
- [API Documentation](#api-documentation)
- [Test Credentials](#test-credentials)
- [Project Structure](#project-structure)
- [Troubleshooting](#troubleshooting)

---

## 🎯 Overview

MoveInSync is an enterprise transportation management system that handles:

- **Multi-tenant architecture** - Supports multiple corporate clients
- **Vendor management** - Manage transportation vendors (Ola, Uber, etc.)
- **Employee transport** - Track employee commute trips
- **Flexible billing** - TRIP-based, PACKAGE-based, or HYBRID models
- **Automated calculations** - Real-time billing with GST
- **Incentive management** - Employee and vendor incentives
- **Comprehensive reports** - Client, Vendor, Employee, and Consolidated reports
- **Role-based access** - ADMIN, CLIENT, VENDOR, EMPLOYEE, FINANCE_TEAM roles

---

## 🏗️ Architecture

### Microservices

| Service | Port | Description |
|---------|------|-------------|
| **Auth Service** | 4005 | JWT authentication & user management |
| **Client Service** | 4010 | Corporate client management |
| **Vendor Service** | 4015 | Transportation vendor management |
| **Trip Service** | 4020 | Trip tracking and management |
| **Billing Service** | 4025 | Billing calculations, reports & invoice generation |
| **Employee Service** | 4035 | Employee management & incentives |

### Frontend

| App | Port | Description |
|-----|------|-------------|
| **React Frontend** | 5173 | User interface (Admin, Client, Vendor, Employee dashboards) |

---

## ⚡ Quick Start

### Method 1: 🐳 Docker (Recommended)

**Prerequisites:**
- **Docker** 20.10+ ([Install Docker](https://docs.docker.com/get-docker/))
- **Docker Compose** 2.0+ (included with Docker Desktop)
- **Node.js 18+** and **npm** (for React frontend)

#### Start Backend Services

```bash
# Navigate to backend folder
cd backend

# Start all services (builds images if needed)
docker-compose up --build -d

# View logs
docker-compose logs -f

# Check service status
docker-compose ps

# Stop services
docker-compose down
```

**All backend services will be available on `localhost:4005-4035`**

#### Start Frontend

```bash
# Navigate to frontend folder
cd frontend

# Install dependencies (first time only)
npm install

# Start development server
npm run dev

# Access at: http://localhost:5173
```

### Method 2: 💻 Local Development (Without Docker)

**Prerequisites:**
- **Java 17+** 
- **Maven 3.8+**
- **Node.js 18+** and **npm**
- **MySQL 8.0+**

#### 1. Setup MySQL Database

```bash
# Start MySQL and ensure it's running on port 3306
# Databases will be auto-created on first service startup
```

**MySQL Configuration:**
- Username: `root`
- Password: `Qwerty@cs12345`
- Port: `3306`

> ⚠️ **Note:** Update passwords in `backend/*/src/main/resources/application.properties`

#### 2. Start Each Service Manually

```bash
# Open 7 separate terminals and run:

# Terminal 1: Auth Service
cd backend/auth-service
mvn spring-boot:run

# Terminal 2: Client Service
cd backend/client-service
mvn spring-boot:run

# Terminal 3: Vendor Service
cd backend/vendor-service
mvn spring-boot:run

# Terminal 4: Trip Service
cd backend/trip-service
mvn spring-boot:run

# Terminal 5: Billing Service
cd backend/billing-service
mvn spring-boot:run

# Terminal 6: Employee Service
cd backend/employee-service
mvn spring-boot:run
```

#### 3. Start Frontend

```bash
cd frontend
npm install
npm run dev
```

---

## 🌐 Services & Ports

All services run on `localhost`:

- **Backend Services:** `4005, 4010, 4015, 4020, 4025, 4035` (6 services)
- **Frontend:** `5173`
- **MySQL:** `3306`

### Health Checks

```bash
# Check if service is running
curl http://localhost:4005/actuator/health   # Auth Service
curl http://localhost:4010/clients          # Client Service
curl http://localhost:4015/vendors          # Vendor Service
curl http://localhost:4020/trips            # Trip Service
curl http://localhost:4025/billing-accounts # Billing Service (includes reports)
curl http://localhost:4035/employees        # Employee Service
```

---

## ✨ Features

### 1. **Multi-Tenant & Role-Based Access**
- Separate data for each corporate client (tenant isolation)
- 5 roles: ADMIN, CLIENT, VENDOR, EMPLOYEE, FINANCE_TEAM
- JWT-based authentication with role-based authorization

### 2. **Flexible Billing Models**
- **TRIP Model:** Pay per trip (₹/km + ₹/hour)
- **PACKAGE Model:** Fixed monthly cost (included KM/hours, extra charges)
- **HYBRID Model:** Combine both models

### 3. **Automated Calculations**
- Real-time trip cost calculation
- GST calculation (5%, 12%, 18%, 28%)
- Base cost + Extra KM + Extra hours
- Monthly billing aggregation

### 4. **Incentive Management**
- Employee incentives based on:
  - Distance traveled
  - Trips completed
  - Off-peak usage bonuses
- Vendor incentives based on:
  - Total trips
  - Distance covered
  - Performance ratings

### 5. **Comprehensive Reporting**
- **Client Reports:** Total costs, trip counts, employee usage
- **Vendor Reports:** Earnings, trip statistics, ratings
- **Employee Reports:** Individual trip history, incentives earned
- **Consolidated Reports:** Cross-client analytics for admins

### 6. **Employee Management**
- Employee profiles with department, designation
- Trip history tracking
- Incentive accumulation
- Active/inactive status management

---

## 🛠️ Tech Stack

### Backend
- **Framework:** Spring Boot 3.4.1
- **Database:** MySQL 8.0 with JPA/Hibernate
- **Security:** Spring Security + JWT
- **Communication:** REST APIs + gRPC
- **Build Tool:** Maven
- **Java Version:** 17+

### Frontend
- **Framework:** React 18
- **Build Tool:** Vite
- **Styling:** Tailwind CSS
- **HTTP Client:** Axios
- **State Management:** React Context API
- **Routing:** React Router v6
- **Notifications:** React Hot Toast

### Database
- **MySQL 8.0+**
- **Schema:** Auto-generated by Hibernate
- **Data Initialization:** `data.sql` scripts with sample data

---

## 💾 Database Setup

Each service creates its own database automatically on first startup:

```sql
-- Databases created automatically:
unified_billing_auth        -- Users, roles, authentication
unified_billing_clients     -- Corporate clients
unified_billing_vendors     -- Transportation vendors
unified_billing_trips       -- Trip records
unified_billing_billing     -- Billing accounts, invoices
unified_billing_analytics   -- Analytics data
unified_billing_employees   -- Employee records
```

**Configuration:**
Each service's `application.properties` has:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/[database_name]?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=Qwerty@cs12345
spring.jpa.hibernate.ddl-auto=update
spring.sql.init.mode=always
```

---

## 📚 API Documentation

### Swagger UI (OpenAPI)

Access interactive API documentation at:

- Auth Service: http://localhost:4005/swagger-ui.html
- Client Service: http://localhost:4010/swagger-ui.html
- Vendor Service: http://localhost:4015/swagger-ui.html
- Trip Service: http://localhost:4020/swagger-ui.html
- Billing Service: http://localhost:4025/swagger-ui.html
- Employee Service: http://localhost:4035/swagger-ui.html

### Sample API Calls

#### 1. Login (Get JWT Token)
```bash
curl -X POST http://localhost:4005/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@moveinsync.com","password":"password"}'

# Returns: {"token":"eyJhbGc...", "role":"ADMIN", ...}
```

#### 2. Get All Clients (with JWT)
```bash
curl -X GET http://localhost:4010/clients \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

#### 3. Create Trip
```bash
curl -X POST http://localhost:4020/trips \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "clientId": "a1111111-1111-1111-1111-111111111111",
    "vendorId": "11111111-1111-1111-1111-111111111111",
    "employeeId": "44444444-4444-4444-4444-444444444444",
    "vehicleNumber": "KA-01-AB-1234",
    "pickupLocation": "Home",
    "dropLocation": "Office",
    "distanceKm": 15.5,
    "tripType": "HOME_TO_OFFICE",
    "status": "COMPLETED"
  }'
```

---

## 🔐 Test Credentials

### Pre-loaded Users

| Role | Email | Password | Description |
|------|-------|----------|-------------|
| **ADMIN** | admin@moveinsync.com | password | System administrator |
| **CLIENT** | admin@amazon.in | password | Amazon India admin |
| **VENDOR** | vendor@ola.com | password | Ola vendor account |

### Pre-loaded Employees

| Name | Email | Company | Trips |
|------|-------|---------|-------|
| Priya Patel | priya.patel@amazon.in | Amazon | 25 |
| Rahul Sharma | rahul.sharma@amazon.in | Amazon | 40 |
| Anita Kumar | anita.kumar@amazon.in | Amazon | 32 |

---

## 📁 Project Structure

```
MoveInSync/
├── backend/                          # Backend microservices (Docker-ready)
│   ├── docker-compose.yml            # Docker orchestration
│   ├── .dockerignore                 # Docker ignore file
│   ├── .gitignore                    # Git ignore file
│   ├── README.md                     # Backend documentation
│   ├── auth-service/                 # Authentication & JWT
│   │   ├── Dockerfile
│   │   ├── pom.xml
│   │   └── src/
│   ├── client-service/               # Client management
│   │   ├── Dockerfile
│   │   ├── pom.xml
│   │   └── src/
│   ├── vendor-service/               # Vendor management
│   │   ├── Dockerfile
│   │   ├── pom.xml
│   │   └── src/
│   ├── trip-service/                 # Trip tracking
│   │   ├── Dockerfile
│   │   ├── pom.xml
│   │   └── src/
│   ├── billing-service/              # Billing & invoicing
│   │   ├── Dockerfile
│   │   ├── pom.xml
│   │   └── src/
│   ├── analytics-service/            # Analytics (gRPC)
│   │   ├── Dockerfile
│   │   ├── pom.xml
│   │   └── src/
│   └── employee-service/             # Employee management
│       ├── Dockerfile
│       ├── pom.xml
│       └── src/
├── frontend/                         # React frontend (Vite)
│   ├── src/
│   │   ├── components/               # Reusable components
│   │   ├── pages/                    # Page components
│   │   ├── contexts/                 # Auth context
│   │   └── utils/                    # API utilities
│   ├── package.json
│   └── README.md
├── QUICK_START_GUIDE.md              # Detailed setup guide
├── EMPLOYEE_ID_VALIDATION_GUIDE.md   # Important: Employee ID best practices
├── MOVEINSYNC_FINAL_COMPLETION_REPORT.md  # Project completion report
└── README.md                         # This file
```

---

## 🐛 Troubleshooting

### Services Won't Start

**Issue:** Port already in use
```bash
# Find process using port (example: 4005)
netstat -ano | findstr :4005

# Kill the process
taskkill /PID <process_id> /F
```

**Issue:** MySQL connection refused
- Ensure MySQL is running: `net start MySQL80`
- Check credentials in `application.properties`
- Verify port `3306` is open

### Database Errors

**Issue:** "Duplicate entry" error
- This is handled automatically with `INSERT IGNORE`
- If issue persists, delete and recreate databases

**Issue:** "Table doesn't exist"
- Ensure `spring.jpa.hibernate.ddl-auto=update` in `application.properties`
- Check `spring.sql.init.mode=always` to run `data.sql`

### Frontend Issues

**Issue:** "Network Error" or "ERR_CONNECTION_REFUSED"
- Ensure all 7 backend services are running
- Check service logs for "Started [Service]Application"
- Verify ports in `frontend/src/utils/api.js` match backend

**Issue:** "Cannot find module" or build errors
```bash
cd frontend
rm -rf node_modules package-lock.json
npm install
npm run dev
```

### Authentication Issues

**Issue:** "Invalid token" or "Unauthorized"
- Token expires after 24 hours (configurable in Auth Service)
- Re-login to get a new token
- Check that Authorization header is: `Bearer YOUR_TOKEN`

### Important: Employee ID Validation

⚠️ **Common Issue:** Trips showing for wrong employee

**Cause:** Using incorrect employee IDs when creating trips

**Solution:** Always fetch employee ID from Employee Service after creation. Never manually type UUIDs!

See `EMPLOYEE_ID_VALIDATION_GUIDE.md` for details.

---

## 📖 Additional Documentation

- **QUICK_START_GUIDE.md** - Detailed step-by-step setup
- **EMPLOYEE_ID_VALIDATION_GUIDE.md** - Best practices for employee IDs
- **MOVEINSYNC_FINAL_COMPLETION_REPORT.md** - Project completion report

---

## 🚀 Next Steps

1. **Configure for Production:**
   - Update MySQL passwords in all `application.properties`
   - Configure proper JWT secret keys
   - Set up HTTPS/SSL
   - Configure CORS for production domain

2. **Add Monitoring:**
   - Spring Boot Actuator endpoints are enabled
   - Add Prometheus + Grafana for metrics
   - Set up ELK stack for log aggregation

3. **Deployment:**
   - Dockerize services (Dockerfiles included in some services)
   - Deploy to Kubernetes or cloud platform
   - Set up CI/CD pipeline

4. **Enhancements:**
   - Add notification service (email/SMS)
   - Implement real-time trip tracking
   - Add mobile app support
   - Enhanced analytics dashboard

---

## 📄 License

This project is for educational/interview purposes as part of the MoveInSync case study.

---

## 🤝 Support

For issues or questions:
1. Check the troubleshooting section above
2. Review service logs in the terminal windows
3. Check `QUICK_START_GUIDE.md` and `EMPLOYEE_ID_VALIDATION_GUIDE.md`

---

**Built with ❤️ for MoveInSync Case Study**
