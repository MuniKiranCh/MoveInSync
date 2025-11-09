# 🚀 MoveInSync - Employee Transportation Management System

A comprehensive full-stack microservices-based platform for managing employee transportation, vendor coordination, and billing operations.

## 📋 Project Overview

MoveInSync is a multi-tenant B2B SaaS platform that helps companies manage their employee transportation needs. The system connects clients (companies), vendors (transportation providers), and employees through a unified platform with role-based access control.

## 🏗️ Architecture

### Backend - Spring Boot Microservices
- **Auth Service** (Port 4005) - JWT authentication, user management, multi-tenancy
- **Client Service** (Port 4010) - Company/client management
- **Vendor Service** (Port 4015) - Transportation vendor management
- **Trip Service** (Port 4020) - Trip scheduling and management
- **Billing Service** (Port 4025) - Invoice and billing operations
- **Analytics Service** (Port 4030) - Reporting and analytics

### Frontend - React with Vite
- Modern React 18 with Hooks
- Tailwind CSS for styling
- React Router for navigation
- Axios for API communication
- Context API for state management

### Database
- **MySQL** - Auth Service (persistent user data)
- **H2 In-Memory** - Other microservices (development)

## 🚀 Quick Start

### Prerequisites
- Java 17+
- Maven 3.6+
- Node.js 16+
- MySQL 8.0+

### 1. Database Setup

```sql
CREATE DATABASE moveinsync_auth;
CREATE USER 'moveinsync'@'localhost' IDENTIFIED BY 'moveinsync123';
GRANT ALL PRIVILEGES ON moveinsync_auth.* TO 'moveinsync'@'localhost';
FLUSH PRIVILEGES;
```

### 2. Start Backend Services

**Option A: Use the batch script (Windows)**
```bash
START_ALL_SERVICES.bat
```

**Option B: Manual start**
```bash
# Auth Service
cd java-spring-microservices-main/auth-service
mvn spring-boot:run

# Client Service
cd java-spring-microservices-main/client-service
mvn spring-boot:run

# Vendor Service
cd java-spring-microservices-main/vendor-service
mvn spring-boot:run

# Trip Service
cd java-spring-microservices-main/trip-service
mvn spring-boot:run

# Billing Service
cd java-spring-microservices-main/billing-service
mvn spring-boot:run

# Analytics Service
cd java-spring-microservices-main/analytics-service
mvn spring-boot:run
```

### 3. Start Frontend

```bash
cd movein-sync-frontend
npm install
npm run dev
```

The application will be available at `http://localhost:5173`

## 🔑 Demo Credentials

| Role | Email | Password |
|------|-------|----------|
| **Super Admin** | admin@moveinsync.com | password |
| **Client Admin** | admin@amazon.in | password |
| **Vendor** | vendor@ola.com | password |

## 🎯 Key Features

### For Super Admins
- ✅ Manage all clients and vendors
- ✅ View platform-wide analytics
- ✅ Monitor all trips and billing
- ✅ User management across tenants

### For Client Companies
- ✅ Manage company employees
- ✅ Assign transportation vendors
- ✅ Configure billing models (subscription/per-trip)
- ✅ View trip history and reports
- ✅ Dashboard with company metrics

### For Vendors
- ✅ View assigned clients
- ✅ Manage trip assignments
- ✅ Track vehicle fleet
- ✅ Invoice generation
- ✅ Performance metrics

### For Employees
- ✅ View personal trip history
- ✅ Book trips (if applicable)
- ✅ Track current rides
- ✅ Access route information

## 📁 Project Structure

```
MoveInSync/
├── java-spring-microservices-main/
│   ├── auth-service/           # Authentication & User Management
│   ├── client-service/         # Client/Company Management
│   ├── vendor-service/         # Vendor Management
│   ├── trip-service/           # Trip & Billing Model Management
│   ├── billing-service/        # Invoice & Billing
│   └── analytics-service/      # Reporting & Analytics
├── movein-sync-frontend/       # React Frontend Application
├── START_ALL_SERVICES.bat      # Windows batch script to start all services
└── README.md                   # This file
```

## 🔧 Technology Stack

### Backend
- **Framework**: Spring Boot 3.x
- **Security**: Spring Security with JWT
- **Database**: MySQL (Auth), H2 (Others)
- **Build Tool**: Maven
- **API**: RESTful APIs
- **Password Hashing**: BCrypt

### Frontend
- **Framework**: React 18
- **Bundler**: Vite
- **Styling**: Tailwind CSS
- **Routing**: React Router v6
- **HTTP Client**: Axios
- **State Management**: Context API
- **Notifications**: React Hot Toast

## 🔐 Security Features

- JWT-based authentication
- Role-based access control (RBAC)
- Multi-tenant data isolation
- BCrypt password hashing
- Secure HTTP-only token storage
- CORS configuration
- Protected API endpoints

## 📊 Data Model

### User Roles
- `ADMIN` - Platform super admin
- `CLIENT` - Company administrator
- `VENDOR` - Transportation vendor
- `EMPLOYEE` - Company employee

### Multi-Tenancy
- Each client company has a unique `tenantId`
- Data isolation enforced at the service level
- Vendors can serve multiple clients
- Employees belong to a single tenant

## 🚧 Development Workflow

1. **Backend Changes**
   - Modify service code
   - Restart specific service
   - Test API endpoint

2. **Frontend Changes**
   - Vite hot-reload enabled
   - Save file to see changes
   - No manual restart needed

3. **Database Changes**
   - Update `data.sql` or `schema.sql`
   - Restart Auth Service
   - MySQL data persists

## 📝 API Endpoints

### Auth Service (4005)
- `POST /login` - User login
- `POST /register` - Client company registration
- `POST /create-user` - Create new user
- `GET /users` - Get all users (Admin)
- `GET /users/tenant/{id}` - Get users by tenant
- `PUT /users/{id}` - Update user details

### Client Service (4010)
- `GET /clients` - List all clients
- `POST /clients` - Create client
- `PUT /clients/{id}` - Update client
- `DELETE /clients/{id}` - Delete client

### Vendor Service (4015)
- `GET /vendors` - List all vendors
- `GET /vendors/active` - List active vendors
- `POST /vendors` - Create vendor
- `PUT /vendors/{id}` - Update vendor

### Trip Service (4020)
- `GET /trips` - List all trips
- `GET /trips/client/{id}` - Trips by client
- `GET /trips/vendor/{id}` - Trips by vendor
- `POST /trips` - Create trip

### Billing Service (4025)
- `GET /billing-models` - List billing models
- `GET /billing-models/client/{id}` - Models by client
- `POST /billing-models` - Create billing model

## 🐛 Troubleshooting

### Services Won't Start
```bash
# Check if ports are in use
netstat -ano | findstr "4005"
netstat -ano | findstr "4010"

# Kill process if needed
taskkill /PID <process_id> /F
```

### Database Connection Issues
```bash
# Test MySQL connection
mysql -u moveinsync -p moveinsync_auth
```

### Frontend Connection Refused
- Ensure all backend services are running
- Check `src/utils/api.js` for correct ports
- Verify CORS is enabled in backend

## 📈 Future Enhancements

- [ ] Real-time trip tracking with WebSocket
- [ ] Mobile app for employees
- [ ] Advanced route optimization
- [ ] Integration with mapping services
- [ ] Automated billing calculations
- [ ] Email notifications
- [ ] Advanced analytics dashboard
- [ ] Multi-language support

## 👥 Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## 📄 License

This project is part of a technical assessment and is for demonstration purposes.

## 📞 Support

For issues or questions:
- Check the documentation in individual service folders
- Review the `*_GUIDE.md` files in the project root
- Contact the development team

---

**Built with ❤️ using Spring Boot and React**
