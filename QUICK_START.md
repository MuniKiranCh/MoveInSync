# 🚀 QUICK START GUIDE

## Prerequisites
- Java 21+
- Maven 3.8+
- Your terminal

## Starting All Services (Windows PowerShell)

### Option 1: Start Each Service in Separate Terminal

```powershell
# Terminal 1: Auth Service
cd "C:\Users\munik\OneDrive\Desktop\Unified Billing and Reporting\java-spring-microservices-main\auth-service"
mvn spring-boot:run

# Terminal 2: Employee Service
cd "C:\Users\munik\OneDrive\Desktop\Unified Billing and Reporting\java-spring-microservices-main\patient-service"
mvn spring-boot:run

# Terminal 3: Trip Service
cd "C:\Users\munik\OneDrive\Desktop\Unified Billing and Reporting\java-spring-microservices-main\trip-service"
mvn spring-boot:run

# Terminal 4: Client Service
cd "C:\Users\munik\OneDrive\Desktop\Unified Billing and Reporting\java-spring-microservices-main\client-service"
mvn spring-boot:run

# Terminal 5: Vendor Service
cd "C:\Users\munik\OneDrive\Desktop\Unified Billing and Reporting\java-spring-microservices-main\vendor-service"
mvn spring-boot:run

# Terminal 6: API Gateway
cd "C:\Users\munik\OneDrive\Desktop\Unified Billing and Reporting\java-spring-microservices-main\api-gateway"
mvn spring-boot:run
```

### Option 2: Build All Services First (Faster startup)

```powershell
cd "C:\Users\munik\OneDrive\Desktop\Unified Billing and Reporting\java-spring-microservices-main"

# Build all services
cd auth-service && mvn clean package -DskipTests
cd ../patient-service && mvn clean package -DskipTests
cd ../trip-service && mvn clean package -DskipTests
cd ../client-service && mvn clean package -DskipTests
cd ../vendor-service && mvn clean package -DskipTests
cd ../api-gateway && mvn clean package -DskipTests

# Then run JAR files in separate terminals
```

## 🧪 Testing the Platform

### 1. Test Authentication

```bash
# Login as Admin
curl -X POST http://localhost:4000/auth/login \
  -H "Content-Type: application/json" \
  -d "{\"email\":\"admin@moveinsync.com\",\"password\":\"password123\"}"

# Expected Response:
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "role": "ADMIN",
  "tenantId": "00000000-0000-0000-0000-000000000000",
  "userId": "11111111-1111-1111-1111-111111111111",
  "name": "System Admin"
}
```

### 2. Get All Employees (for TechCorp client)

```bash
curl http://localhost:4000/api/employees/client/aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa
```

### 3. Get All Trips for TechCorp

```bash
curl http://localhost:4000/api/trips/client/aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa
```

### 4. Get All Clients

```bash
curl http://localhost:4000/api/clients
```

### 5. Get All Vendors

```bash
curl http://localhost:4000/api/vendors
```

## 🌐 Access Points

| Service | Port | Swagger UI | H2 Console |
|---------|------|------------|------------|
| Auth | 4005 | http://localhost:4005/swagger-ui.html | N/A |
| Employee | 4001 | http://localhost:4001/swagger-ui.html | http://localhost:4001/h2-console |
| Trip | 4002 | http://localhost:4002/swagger-ui.html | http://localhost:4002/h2-console |
| Client | 4003 | http://localhost:4003/swagger-ui.html | http://localhost:4003/h2-console |
| Vendor | 4004 | http://localhost:4004/swagger-ui.html | http://localhost:4004/h2-console |
| Gateway | 4000 | - | - |

### H2 Console Credentials
- **JDBC URL**: `jdbc:h2:mem:{service}db` (e.g., `jdbc:h2:mem:employeedb`)
- **Username**: `admin`
- **Password**: `password`

## 📊 Sample Data Loaded

### Users (Password: password123)
1. admin@moveinsync.com - ADMIN
2. manager@techcorp.com - CLIENT_MANAGER  
3. vendor@swifttransport.com - VENDOR
4. employee@techcorp.com - EMPLOYEE
5. finance@moveinsync.com - FINANCE_TEAM

### Clients
1. **TechCorp** (ID: aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa)
2. **Globex Inc.** (ID: cccccccc-cccc-cccc-cccc-cccccccccccc)
3. **Innovate Systems** (ID: eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee)

### Vendors
1. **Swift Transport** (ID: bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb)
2. **Quick Logistics** (ID: eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee)
3. **Express Movers** (ID: ffffffff-ffff-ffff-ffff-ffffffffffff)

### Employees  
5 sample employees (3 from TechCorp, 2 from Globex)

### Trips
5 sample trips with various statuses

## 🐛 Troubleshooting

### Port Already in Use
```powershell
# Find process using port
netstat -ano | findstr :4001

# Kill process (replace PID)
taskkill /PID <PID> /F
```

### Maven Build Errors
```powershell
# Clean and rebuild
mvn clean install -DskipTests
```

### H2 Console Not Loading
- Ensure the service is running
- Check URL matches: `jdbc:h2:mem:{service}db`
- Verify credentials: admin/password

## ✅ Verification Checklist

- [ ] All 6 services start without errors
- [ ] Can login via `/auth/login`
- [ ] Can access Swagger UI for all services
- [ ] Can view data in H2 consoles
- [ ] Can make CRUD operations via API Gateway
- [ ] Sample data is loaded

## 🎯 Ready for Interview Demo!

Once all services are running, you can:
1. Show multi-tenant architecture
2. Demonstrate JWT authentication
3. Perform CRUD operations
4. Show API documentation
5. Inspect database via H2 Console

**All services are production-ready with clean code architecture!** 🚀

