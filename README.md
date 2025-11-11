# 🚀 MoveInSync - Unified Billing & Reporting Platform

A complete **microservices-based** transportation management and billing platform for corporate employee transport services. Built with **Spring Boot** (backend) and **React + Vite** (frontend).

---

## 📋 Table of Contents

- [Overview](#overview)
- [System Architecture](#system-architecture)
- [How Each Service Works](#how-each-service-works)
- [Billing Calculation Engine](#billing-calculation-engine)
- [Data Flow & Integration](#data-flow--integration)
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

## 🏗️ System Architecture

### Microservices Overview

This application follows a **microservices architecture** where each service is independent, has its own database, and communicates via REST APIs. This ensures:
- **Scalability**: Each service can be scaled independently
- **Maintainability**: Changes in one service don't affect others
- **Resilience**: Failure in one service doesn't bring down the entire system

```
┌─────────────────────────────────────────────────────────────────┐
│                       React Frontend (5173)                      │
│           Admin | Client | Vendor | Employee Dashboards         │
└────────────┬────────────────────────────────────────────────────┘
             │
             │ HTTP/REST API Calls
             │
┌────────────┴────────────────────────────────────────────────────┐
│                     API Gateway Layer (JWT Auth)                 │
└─┬──────┬────────┬────────┬───────────┬────────────┬─────────────┘
  │      │        │        │           │            │
  │      │        │        │           │            │
┌─┴──┐ ┌─┴───┐ ┌─┴───┐  ┌─┴────┐  ┌───┴────┐  ┌────┴─────┐
│Auth│ │Client│ │Vendor│ │ Trip │  │Billing │  │Employee  │
│4005│ │ 4010 │ │ 4015 │ │ 4020 │  │ 4025   │  │  4035    │
└─┬──┘ └──┬───┘ └──┬───┘  └──┬───┘  └───┬────┘  └────┬─────┘
  │       │        │         │          │            │
  │       │        │         │          │            │
┌─┴───────┴────────┴─────────┴──────────┴────────────┴─────────┐
│            MySQL Database (Separate DB per service)            │
│  auth_db | clients_db | vendors_db | trips_db | billing_db    │
│                      employees_db                              │
└────────────────────────────────────────────────────────────────┘
```

### Service Communication

| Service | Port | Database | Dependencies |
|---------|------|----------|--------------|
| **Auth Service** | 4005 | unified_billing_auth | None |
| **Client Service** | 4010 | unified_billing_clients | Auth |
| **Vendor Service** | 4015 | unified_billing_vendors | Auth |
| **Trip Service** | 4020 | unified_billing_trips | Auth, Client, Vendor, Employee |
| **Billing Service** | 4025 | unified_billing_billing | Auth, Trip, Client, Vendor |
| **Employee Service** | 4035 | unified_billing_employees | Auth, Client |

---

## 🔧 How Each Service Works

### 1. 🔐 Auth Service (Port 4005)

**Purpose**: Centralized authentication and authorization for all services

**Key Responsibilities:**
- User registration and login
- JWT token generation and validation
- Role-based access control (ADMIN, CLIENT, VENDOR, EMPLOYEE, FINANCE_TEAM)
- Password encryption using BCrypt

**How It Works:**
1. **Registration**: 
   - User provides email, password, name, and role
   - Password is hashed using BCrypt
   - User record is stored in `users` table
   - Returns user details (password excluded)

2. **Login**:
   - User provides email and password
   - System verifies credentials against hashed password
   - Generates JWT token with user ID, email, and role
   - Token valid for 24 hours (configurable)

3. **Authorization**:
   - Every API request includes JWT token in Authorization header
   - Each service validates token before processing request
   - Role-based access enforced at endpoint level

**Key Entities:**
- `User`: id, email, password (hashed), name, role, createdAt

**API Endpoints:**
```
POST /auth/register - Register new user
POST /auth/login    - Login and get JWT token
GET  /auth/me       - Get current user info
```

---

### 2. 🏢 Client Service (Port 4010)

**Purpose**: Manage corporate clients (companies) who use the transportation platform

**Key Responsibilities:**
- Client (company) registration and management
- Billing model configuration per client
- Track client status (ACTIVE, SUSPENDED, PENDING_APPROVAL)
- Maintain client contact and address information

**How It Works:**
1. **Client Creation**:
   - Admin creates client with company details
   - Selects billing model (TRIP, PACKAGE, HYBRID)
   - Sets billing parameters (rates, GST, payment terms)
   - Client status set to ACTIVE

2. **Billing Model Configuration**:
   - **TRIP Model**: Pay-per-trip pricing
     - Rate per KM
     - Rate per hour
     - Base fare
   - **PACKAGE Model**: Fixed monthly fee
     - Included KM and hours
     - Extra KM/hour charges
   - **HYBRID Model**: Combination of both

3. **Client Management**:
   - Update client details
   - Change billing model
   - Suspend/activate clients
   - Track total spending

**Key Entities:**
- `Client`: id, companyName, email, phone, address, billingModel, status, gstNumber
- `BillingAccount`: clientId, billingModel, ratePerKm, ratePerHour, baseFare, gstRate

**API Endpoints:**
```
POST   /clients        - Create new client
GET    /clients        - List all clients
GET    /clients/{id}   - Get client details
PUT    /clients/{id}   - Update client
DELETE /clients/{id}   - Delete client
```

---

### 3. 🚗 Vendor Service (Port 4015)

**Purpose**: Manage transportation vendors (Ola, Uber, local cab services)

**Key Responsibilities:**
- Vendor registration and onboarding
- Fleet management (vehicles, drivers)
- Performance tracking (ratings, trips completed)
- Payment status management

**How It Works:**
1. **Vendor Registration**:
   - Vendor provides company details
   - Adds fleet information (vehicle count, types)
   - Sets service rates and availability
   - Status initially PENDING_APPROVAL

2. **Fleet Management**:
   - Add/remove vehicles
   - Assign drivers
   - Track vehicle availability
   - Manage service areas

3. **Performance Tracking**:
   - Average rating from trips
   - Total trips completed
   - Total distance covered
   - Payment pending/received status

**Key Entities:**
- `Vendor`: id, companyName, email, phone, fleetSize, rating, status, paymentStatus
- `Vehicle`: id, vendorId, vehicleNumber, type, capacity, isAvailable

**API Endpoints:**
```
POST   /vendors        - Register new vendor
GET    /vendors        - List all vendors
GET    /vendors/{id}   - Get vendor details
PUT    /vendors/{id}   - Update vendor
DELETE /vendors/{id}   - Delete vendor
GET    /vendors/{id}/vehicles - Get vendor fleet
```

---

### 4. 🚕 Trip Service (Port 4020)

**Purpose**: Core service for managing and tracking all transportation trips

**Key Responsibilities:**
- Trip creation and scheduling
- Real-time trip tracking
- Trip status management (REQUESTED, IN_PROGRESS, COMPLETED, CANCELLED)
- Calculate trip cost based on client's billing model
- Assign vendors to trips

**How It Works:**
1. **Trip Creation**:
   - Employee or admin creates trip request
   - Provides: pickup location, drop location, date/time
   - System assigns available vendor
   - Trip status: REQUESTED

2. **Trip Execution**:
   - Driver accepts trip: Status → IN_PROGRESS
   - Records: start time, start odometer
   - Trip in progress
   - Records: end time, end odometer, distance
   - Status → COMPLETED

3. **Cost Calculation** (Critical Part):
   - Fetches client's billing model
   - **If TRIP Model**:
     ```
     Base Cost = Distance (km) × Rate per KM
     Time Cost = Duration (hours) × Rate per Hour
     Total = Base Cost + Time Cost
     GST = Total × GST Rate
     Final Amount = Total + GST
     ```
   - **If PACKAGE Model**:
     ```
     Base = Monthly Package Cost
     Extra KM = max(0, Distance - Included KM) × Extra KM Rate
     Extra Hours = max(0, Duration - Included Hours) × Extra Hour Rate
     Total = Base + Extra KM + Extra Hours
     GST = Total × GST Rate
     Final Amount = Total + GST
     ```
   - **If HYBRID**: Combination logic

4. **Trip Assignment Logic**:
   - Finds available vendors in service area
   - Checks vehicle availability
   - Considers vendor ratings
   - Assigns highest-rated available vendor

**Key Entities:**
- `Trip`: id, clientId, vendorId, employeeId, pickupLocation, dropLocation, 
         distanceKm, durationMinutes, status, tripCost, tripDate, vehicleNumber
- `TripStatus`: REQUESTED, SCHEDULED, IN_PROGRESS, COMPLETED, CANCELLED

**API Endpoints:**
```
POST   /trips              - Create new trip
GET    /trips              - List all trips
GET    /trips/{id}         - Get trip details
PUT    /trips/{id}         - Update trip
DELETE /trips/{id}         - Cancel trip
GET    /trips/client/{id}  - Get client's trips
GET    /trips/vendor/{id}  - Get vendor's trips
GET    /trips/employee/{id}- Get employee's trips
```

---

### 5. 💰 Billing Service (Port 4025)

**Purpose**: Handle all billing, invoicing, and financial reporting

**Key Responsibilities:**
- Aggregate monthly billing for clients
- Generate invoices
- Calculate vendor payments
- Generate comprehensive financial reports
- Track payment status

**How It Works:**

#### A. **Monthly Billing Aggregation**:
1. **Data Collection**:
   - Fetches all COMPLETED trips for a client in billing period
   - Groups trips by month and client
   - Retrieves client's billing model

2. **Calculation Process**:
   ```
   FOR each client:
     total_cost = 0
     total_trips = 0
     total_distance = 0
     total_duration = 0
     
     FOR each trip in month:
       IF billing_model == "TRIP":
         trip_cost = (distance × rate_per_km) + (duration × rate_per_hour)
       
       ELSE IF billing_model == "PACKAGE":
         base_cost = monthly_package_cost
         extra_km = max(0, total_distance - included_km)
         extra_hours = max(0, total_duration - included_hours)
         trip_cost = base_cost + (extra_km × extra_km_rate) + (extra_hours × extra_hour_rate)
       
       ELSE IF billing_model == "HYBRID":
         // Hybrid logic combining both models
       
       total_cost += trip_cost
       total_trips += 1
       total_distance += distance
       total_duration += duration
     
     subtotal = total_cost
     gst_amount = subtotal × gst_rate
     grand_total = subtotal + gst_amount
     
     CREATE invoice(client, subtotal, gst_amount, grand_total, due_date)
   ```

3. **Invoice Generation**:
   - Creates invoice document
   - Includes: Invoice number, date, line items, subtotal, GST, total
   - Status: PENDING
   - Sets due date (typically 30 days)

#### B. **Vendor Payment Calculation**:
```
FOR each vendor:
  total_earnings = 0
  total_trips = 0
  
  FOR each completed trip:
    // Vendor gets percentage of trip cost
    vendor_share = trip_cost × vendor_commission_rate (e.g., 85%)
    
    // Add incentives
    IF total_trips > 100:
      incentive = total_earnings × incentive_rate (e.g., 5%)
      vendor_share += incentive
    
    total_earnings += vendor_share
    total_trips += 1
  
  CREATE vendor_payment(vendor, amount, trips, status: PENDING)
```

#### C. **Report Generation**:

**Client Report**:
- Total trips taken
- Total distance covered
- Total amount paid
- Average cost per trip
- Monthly trends
- Employee-wise breakup

**Vendor Report**:
- Total trips completed
- Total earnings
- Average rating
- Payment status
- Performance metrics

**Employee Report**:
- Individual trip history
- Total trips
- Total distance
- Incentives earned

**Consolidated Report** (Admin only):
- Overall platform statistics
- Total revenue
- Active clients/vendors
- Trip trends
- Top performers

**Key Entities:**
- `BillingAccount`: clientId, billingModel, accountBalance, billingCycle
- `Invoice`: id, clientId, invoiceNumber, amount, gstAmount, totalAmount, status, dueDate
- `VendorPayment`: id, vendorId, amount, period, status, paidDate
- `Report`: type, clientId, vendorId, period, data (JSON)

**API Endpoints:**
```
POST   /billing-accounts             - Create billing account
GET    /billing-accounts/{clientId}  - Get client's billing account
POST   /invoices/generate            - Generate monthly invoices
GET    /invoices/client/{clientId}   - Get client invoices
POST   /vendor-payments/calculate    - Calculate vendor payments
GET    /reports/client/{clientId}    - Generate client report
GET    /reports/vendor/{vendorId}    - Generate vendor report
GET    /reports/consolidated         - Generate consolidated report
```

---

### 6. 👥 Employee Service (Port 4035)

**Purpose**: Manage employee profiles and incentive programs

**Key Responsibilities:**
- Employee registration and profile management
- Track employee trip history
- Calculate and manage incentives
- Department and designation management
- Active/inactive status tracking

**How It Works:**

1. **Employee Registration**:
   - Client admin creates employee
   - Provides: name, email, phone, department, designation
   - Links to client (company)
   - Status: ACTIVE

2. **Incentive Calculation**:
   ```
   FOR each employee:
     total_incentive = 0
     
     // Distance-based incentive
     IF total_distance > 500_km:
       distance_incentive = ₹500
     ELSE IF total_distance > 200_km:
       distance_incentive = ₹200
     
     // Trip count incentive
     IF total_trips > 50:
       trip_incentive = ₹1000
     ELSE IF total_trips > 20:
       trip_incentive = ₹500
     
     // Off-peak usage bonus (trips between 10 AM - 4 PM)
     IF off_peak_trips > 10:
       off_peak_bonus = ₹300
     
     total_incentive = distance_incentive + trip_incentive + off_peak_bonus
     
     UPDATE employee SET incentive_earned = total_incentive
   ```

3. **Trip History Tracking**:
   - Fetches all trips for employee from Trip Service
   - Calculates statistics:
     - Total trips
     - Total distance
     - Favorite routes
     - Most used vendors
     - Monthly usage patterns

**Key Entities:**
- `Employee`: id, clientId, name, email, phone, department, designation, 
             status, totalTrips, totalDistance, incentiveEarned

**API Endpoints:**
```
POST   /employees                     - Create employee
GET    /employees                     - List employees
GET    /employees/{id}                - Get employee details
PUT    /employees/{id}                - Update employee
DELETE /employees/{id}                - Deactivate employee
GET    /employees/client/{clientId}   - Get client's employees
POST   /employees/{id}/calculate-incentive - Calculate incentive
```

---

## 💵 Billing Calculation Engine

### Detailed Billing Models Explained

#### 1. **TRIP-BASED Model** (Pay Per Trip)

**Use Case**: Clients with variable trip requirements

**Parameters**:
- Base Fare: ₹50
- Rate per KM: ₹12
- Rate per Hour: ₹80
- GST: 18%

**Calculation Example**:
```
Trip Details:
- Distance: 25 km
- Duration: 1.5 hours

Calculation:
Base Cost = ₹50
Distance Cost = 25 km × ₹12/km = ₹300
Time Cost = 1.5 hours × ₹80/hour = ₹120

Subtotal = ₹50 + ₹300 + ₹120 = ₹470
GST (18%) = ₹470 × 0.18 = ₹84.60
Total = ₹470 + ₹84.60 = ₹554.60
```

#### 2. **PACKAGE-BASED Model** (Fixed Monthly Fee)

**Use Case**: Clients with predictable, regular transportation needs

**Parameters**:
- Monthly Package: ₹50,000
- Included: 1000 km, 100 hours
- Extra KM Rate: ₹15/km
- Extra Hour Rate: ₹100/hour
- GST: 18%

**Calculation Example (Month Usage)**:
```
Monthly Usage:
- Total Distance: 1200 km
- Total Hours: 120 hours

Calculation:
Base Package = ₹50,000

Extra KM = 1200 - 1000 = 200 km
Extra KM Cost = 200 × ₹15 = ₹3,000

Extra Hours = 120 - 100 = 20 hours
Extra Hours Cost = 20 × ₹100 = ₹2,000

Subtotal = ₹50,000 + ₹3,000 + ₹2,000 = ₹55,000
GST (18%) = ₹55,000 × 0.18 = ₹9,900
Total = ₹55,000 + ₹9,900 = ₹64,900
```

**If Under Limit**:
```
Monthly Usage:
- Total Distance: 800 km (under 1000)
- Total Hours: 80 hours (under 100)

Calculation:
Base Package = ₹50,000
Extra Charges = ₹0 (under included limits)

Subtotal = ₹50,000
GST (18%) = ₹9,000
Total = ₹59,000
```

#### 3. **HYBRID Model** (Combination)

**Use Case**: Flexible pricing for varied usage patterns

**Parameters**:
- Base Monthly: ₹20,000
- Included: 500 km, 50 hours
- Beyond limit: TRIP-based pricing
  - Extra KM Rate: ₹12/km
  - Extra Hour Rate: ₹80/hour
- GST: 18%

**Calculation Example**:
```
Monthly Usage:
- Total Distance: 750 km
- Total Hours: 75 hours

Calculation:
Base Package = ₹20,000

Extra KM = 750 - 500 = 250 km
Extra KM Cost = 250 × ₹12 = ₹3,000

Extra Hours = 75 - 50 = 25 hours
Extra Hours Cost = 25 × ₹80 = ₹2,000

Subtotal = ₹20,000 + ₹3,000 + ₹2,000 = ₹25,000
GST (18%) = ₹25,000 × 0.18 = ₹4,500
Total = ₹25,000 + ₹4,500 = ₹29,500
```

### Vendor Payment Calculation

```
Vendor Commission Structure:
- Base Commission: 85% of trip cost
- Bonus for 100+ trips/month: +5%
- Bonus for 4.5+ rating: +3%

Example:
Trip Revenue = ₹554.60 (from above example)

Base Commission = ₹554.60 × 0.85 = ₹471.41

IF trips_this_month >= 100:
  Bonus = ₹471.41 × 0.05 = ₹23.57
  
IF avg_rating >= 4.5:
  Rating Bonus = ₹471.41 × 0.03 = ₹14.14

Total Vendor Payment = ₹471.41 + ₹23.57 + ₹14.14 = ₹509.12
```

---

## 🔄 Data Flow & Integration

### Complete Trip Lifecycle

```
1. TRIP CREATION
   Employee/Admin → Trip Service
   ↓
   Trip Service validates:
   - Employee exists (Employee Service)
   - Client exists (Client Service)
   - Vendor available (Vendor Service)
   ↓
   Creates trip with status: REQUESTED

2. TRIP ASSIGNMENT
   Trip Service → Vendor Service
   ↓
   Finds available vendor
   ↓
   Updates trip: status = SCHEDULED, assignedVendor

3. TRIP EXECUTION
   Driver app → Trip Service
   ↓
   Status: IN_PROGRESS
   ↓
   Records start time, location
   ↓
   Trip in progress
   ↓
   Records end time, location, distance
   ↓
   Status: COMPLETED

4. COST CALCULATION
   Trip Service → Client Service (get billing model)
   ↓
   Calculates trip cost based on model
   ↓
   Updates trip with tripCost

5. BILLING AGGREGATION
   Billing Service (scheduled monthly)
   ↓
   Fetches all completed trips → Trip Service
   ↓
   Groups by client
   ↓
   Calculates monthly total
   ↓
   Generates invoice
   ↓
   Status: PENDING

6. PAYMENT PROCESSING
   Client pays invoice
   ↓
   Invoice status: PAID
   ↓
   Triggers vendor payment calculation
   ↓
   Vendor Payment status: PENDING → PAID

7. REPORTING
   User requests report → Billing Service
   ↓
   Fetches data from multiple services:
   - Trips (Trip Service)
   - Client details (Client Service)
   - Vendor details (Vendor Service)
   - Employee stats (Employee Service)
   ↓
   Aggregates and formats data
   ↓
   Returns comprehensive report
```

### Service Communication Matrix

| Service | Calls | Purpose |
|---------|-------|---------|
| **Frontend** | Auth | Login, authentication |
| **Frontend** | Client | CRUD clients |
| **Frontend** | Vendor | CRUD vendors |
| **Frontend** | Trip | Create, track trips |
| **Frontend** | Billing | View invoices, reports |
| **Frontend** | Employee | CRUD employees |
| **Trip Service** | Client | Validate client, get billing model |
| **Trip Service** | Vendor | Check availability, assign |
| **Trip Service** | Employee | Validate employee |
| **Billing Service** | Trip | Fetch trips for billing period |
| **Billing Service** | Client | Get client details |
| **Billing Service** | Vendor | Get vendor details |
| **Billing Service** | Employee | Get employee stats |
| **All Services** | Auth | Validate JWT token |

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
- Username: `root` (or your MySQL username)
- Password: `[YOUR_PASSWORD]` (set in environment variables)
- Port: `3306`

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
spring.datasource.username=${DB_USERNAME:root}
spring.datasource.password=${DB_PASSWORD}
spring.jpa.hibernate.ddl-auto=update
spring.sql.init.mode=always
```

> ⚠️ **Security**: Set `DB_USERNAME` and `DB_PASSWORD` as environment variables. Never commit credentials to Git!

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

**Built with ❤️ for MoveInSync Case Study**
