# Quick Connection Test - Frontend to Auth Service

## ✅ Configuration Complete

### Backend (Auth Service)
- **Port**: 4005
- **CORS**: Enabled for http://localhost:3000
- **Endpoints**:
  - POST `/login` - User authentication
  - GET `/validate` - Token validation

### Frontend
- **Port**: 3000
- **API Base URL**: http://localhost:4005
- **No API Gateway** (bypassed for now)

## 🧪 Testing Steps

### Step 1: Verify Auth Service is Running

Check the auth-service console for these messages:
```
✅ HikariPool-1 - Start completed
✅ Initialized JPA EntityManagerFactory
✅ Started AuthServiceApplication
✅ Tomcat started on port(s): 4005
```

### Step 2: Test API Directly

Open PowerShell and test the login endpoint:

```powershell
# Test 1: Check if service is accessible
curl http://localhost:4005/login

# Test 2: Try login with admin credentials
curl -X POST http://localhost:4005/login `
  -H "Content-Type: application/json" `
  -d '{\"username\":\"admin\",\"password\":\"admin123\"}'
```

**Expected Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIs...",
  "username": "admin",
  "role": "ADMIN",
  "email": "admin@movein sync.com"
}
```

### Step 3: Test Frontend Connection

1. Make sure auth-service is running on port 4005
2. Start the React frontend:
   ```bash
   cd movein-sync-frontend
   npm run dev
   ```
3. Open browser: http://localhost:3000
4. Try logging in with:
   - Username: `admin`
   - Password: `admin123`

### Step 4: Check Browser Console

If successful, you should see:
- ✅ `POST http://localhost:4005/login` - Status 200
- ✅ Token stored in localStorage
- ✅ Redirect to dashboard

If there are errors:
- ❌ `ERR_CONNECTION_REFUSED` - Auth service not running
- ❌ `CORS error` - CORS configuration issue
- ❌ `401 Unauthorized` - Wrong credentials

## 🔧 Troubleshooting

### Issue 1: ERR_CONNECTION_REFUSED
**Cause**: Auth service not running on port 4005

**Solution**:
```bash
cd java-spring-microservices-main/auth-service
./mvnw.cmd spring-boot:run
```

### Issue 2: CORS Error
**Cause**: CORS not properly configured

**Solution**: Check SecurityConfig.java has corsConfigurationSource() method
- Should allow origins: http://localhost:3000
- Should allow methods: GET, POST, PUT, DELETE, PATCH, OPTIONS

### Issue 3: 401 Unauthorized
**Cause**: Invalid credentials or database not initialized

**Solution**:
1. Check MySQL is running: `Get-Service -Name MySQL*`
2. Check database exists: `mysql -u root -proot -e "USE unified_billing_auth; SELECT * FROM users;"`
3. Verify credentials in database

### Issue 4: Port Already in Use
**Cause**: Something else is using port 4005

**Solution**:
```powershell
# Find process using port 4005
netstat -ano | findstr :4005

# Kill the process (replace PID with actual process ID)
taskkill /PID <PID> /F
```

## 📝 Current Architecture

```
React Frontend (Port 3000)
        ↓
   HTTP Requests
        ↓
Auth Service (Port 4005)
        ↓
    MySQL Database
```

## ✅ What Changed

1. **Frontend** (`src/utils/api.js`):
   - Changed API_BASE_URL from `http://localhost:8080/api` to `http://localhost:4005`
   - Now connects directly to auth-service

2. **Backend** (`SecurityConfig.java`):
   - Added CORS configuration
   - Allows requests from http://localhost:3000
   - Permits all HTTP methods

## 🎯 Next Steps

1. ✅ Start auth-service (port 4005)
2. ✅ Start frontend (port 3000)
3. ✅ Test login functionality
4. ⏳ If successful, add other services later
5. ⏳ Eventually integrate API Gateway

## 📞 Quick Test Commands

```powershell
# Test 1: Service Health
curl http://localhost:4005/login

# Test 2: Admin Login
$body = @{
    username = "admin"
    password = "admin123"
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:4005/login" `
  -Method Post `
  -Body $body `
  -ContentType "application/json"

# Test 3: Vendor Login
$body = @{
    username = "vendor"
    password = "vendor123"
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:4005/login" `
  -Method Post `
  -Body $body `
  -ContentType "application/json"
```

---

**Status**: Ready to test! 🚀

**What to do now**:
1. Ensure auth-service is running (check console for "Started AuthServiceApplication")
2. Ensure frontend dev server is running (npm run dev)
3. Open http://localhost:3000 and try logging in

