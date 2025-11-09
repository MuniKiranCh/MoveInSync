# ✅ Register Endpoint Created!

## 🎉 New Endpoint Available

**POST** `http://localhost:4005/register`

## 📝 How to Register Users

### Option 1: Using PowerShell (Quick)

Wait for the auth-service to start (watch for "Started AuthServiceApplication"), then run:

```powershell
# Register Admin User
$admin = @{
    email = "admin@moveinsync.com"
    password = "admin123"
    firstName = "System"
    lastName = "Admin"
    role = "ADMIN"
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:4005/register" -Method Post -Body $admin -ContentType "application/json"

# Register Vendor User
$vendor = @{
    email = "vendor@swifttransport.com"
    password = "vendor123"
    firstName = "John"
    lastName = "Vendor"
    role = "VENDOR"
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:4005/register" -Method Post -Body $vendor -ContentType "application/json"

# Register Employee User
$employee = @{
    email = "employee@techcorp.com"
    password = "employee123"
    firstName = "Bob"
    lastName = "Employee"
    role = "EMPLOYEE"
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:4005/register" -Method Post -Body $employee -ContentType "application/json"
```

### Option 2: Using curl

```bash
# Register Admin
curl -X POST http://localhost:4005/register \
  -H "Content-Type: application/json" \
  -d "{\"email\":\"admin@moveinsync.com\",\"password\":\"admin123\",\"firstName\":\"System\",\"lastName\":\"Admin\",\"role\":\"ADMIN\"}"

# Register Vendor
curl -X POST http://localhost:4005/register \
  -H "Content-Type: application/json" \
  -d "{\"email\":\"vendor@swifttransport.com\",\"password\":\"vendor123\",\"firstName\":\"John\",\"lastName\":\"Vendor\",\"role\":\"VENDOR\"}"

# Register Employee
curl -X POST http://localhost:4005/register \
  -H "Content-Type: application/json" \
  -d "{\"email\":\"employee@techcorp.com\",\"password\":\"employee123\",\"firstName\":\"Bob\",\"lastName\":\"Employee\",\"role\":\"EMPLOYEE\"}"
```

### Option 3: Using Postman or similar tool

**URL**: `http://localhost:4005/register`
**Method**: POST
**Headers**: `Content-Type: application/json`
**Body** (JSON):
```json
{
  "email": "admin@moveinsync.com",
  "password": "admin123",
  "firstName": "System",
  "lastName": "Admin",
  "role": "ADMIN"
}
```

## 📋 Request Format

```json
{
  "email": "user@example.com",      // Required, must be valid email
  "password": "password123",        // Required, min 8 characters
  "firstName": "First",             // Required
  "lastName": "Last",               // Required
  "role": "ADMIN"                   // Required: ADMIN, VENDOR, or EMPLOYEE
}
```

## ✅ Success Response (201 Created)

```json
{
  "token": "eyJhbGciOiJIUzI1NiIs...",
  "role": "ADMIN",
  "tenantId": "12345678-1234-1234-1234-123456789012",
  "userId": "12345678-1234-1234-1234-123456789012",
  "name": "System Admin"
}
```

User is automatically logged in and you get a JWT token!

## ❌ Error Responses

### 409 Conflict
```
User already exists with this email
```

### 400 Bad Request
```json
{
  "message": "Validation failed",
  "errors": ["Email is required", "Password must be at least 8 characters"]
}
```

## 🎯 Available Roles

- **ADMIN** - Full system access
- **VENDOR** - Vendor dashboard access
- **EMPLOYEE** - Employee dashboard access

## 🚀 Quick Setup Script

Save this as `register-users.ps1`:

```powershell
Write-Host "Registering sample users..." -ForegroundColor Cyan

# Admin
$admin = @{ email = "admin@moveinsync.com"; password = "admin123"; firstName = "System"; lastName = "Admin"; role = "ADMIN" } | ConvertTo-Json
try {
    $result = Invoke-RestMethod -Uri "http://localhost:4005/register" -Method Post -Body $admin -ContentType "application/json"
    Write-Host "✅ Admin registered!" -ForegroundColor Green
} catch {
    Write-Host "❌ Admin: $($_.Exception.Message)" -ForegroundColor Red
}

# Vendor
$vendor = @{ email = "vendor@swifttransport.com"; password = "vendor123"; firstName = "John"; lastName = "Vendor"; role = "VENDOR" } | ConvertTo-Json
try {
    $result = Invoke-RestMethod -Uri "http://localhost:4005/register" -Method Post -Body $vendor -ContentType "application/json"
    Write-Host "✅ Vendor registered!" -ForegroundColor Green
} catch {
    Write-Host "❌ Vendor: $($_.Exception.Message)" -ForegroundColor Red
}

# Employee
$employee = @{ email = "employee@techcorp.com"; password = "employee123"; firstName = "Bob"; lastName = "Employee"; role = "EMPLOYEE" } | ConvertTo-Json
try {
    $result = Invoke-RestMethod -Uri "http://localhost:4005/register" -Method Post -Body $employee -ContentType "application/json"
    Write-Host "✅ Employee registered!" -ForegroundColor Green
} catch {
    Write-Host "❌ Employee: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`n🎉 Done! You can now login at http://localhost:3000" -ForegroundColor Green
```

## 📝 After Registration

Once users are registered, you can login with:
- Email: `admin@moveinsync.com`
- Password: `admin123`

Go to `http://localhost:3000` and try logging in!

---

**Status**: Auth service restarting with register endpoint... 🚀

**Next Steps**:
1. Wait for "Started AuthServiceApplication" in the CMD window
2. Run the PowerShell commands above to register users
3. Login at http://localhost:3000

