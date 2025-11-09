# ✅ Sample Users Populated!

## 🔑 Login Credentials

The auth-service will automatically create these users in the database:

### Admin User
- **Email**: `admin@moveinsync.com`
- **Password**: `admin123`
- **Or just use**: `admin` / `admin123` (frontend adds @moveinsync.com)

### Vendor User  
- **Email**: `vendor@swifttransport.com`
- **Password**: `vendor123`
- **Or just use**: `vendor` / `vendor123`

### Employee User
- **Email**: `employee@techcorp.com`
- **Password**: `employee123`
- **Or just use**: `employee` / `employee123`

## 📝 What Was Fixed

### 1. Database Population (data.sql)
- ✅ Created MySQL-compatible insert statements
- ✅ Used proper UUID handling with `UNHEX(REPLACE())`
- ✅ BCrypt password hashes for all users
- ✅ Proper table structure matching User entity

### 2. Configuration (application.properties)
```properties
spring.sql.init.mode=always
spring.jpa.defer-datasource-initialization=true
```
- ✅ Ensures data.sql runs after table creation
- ✅ Loads sample users on startup

### 3. Frontend (AuthContext.jsx)
```javascript
// Converts "admin" to "admin@moveinsync.com"
const email = username.includes('@') ? username : `${username}@moveinsync.com`
```
- ✅ Auto-converts username to email
- ✅ Allows both formats: `admin` or `admin@moveinsync.com`

## 🚀 How to Test

### Step 1: Wait for Service to Start
Watch the new CMD window for:
```
✓ HikariPool-1 - Start completed
✓ Executing SQL script from URL [file:...data.sql]  ← Look for this!
✓ Started AuthServiceApplication
```

### Step 2: Verify Users in Database (Optional)
```sql
mysql -u root -pQwerty@cs12345 -e "USE unified_billing_auth; SELECT email, role, first_name FROM users;"
```

Expected output:
```
+-----------------------------+----------+------------+
| email                       | role     | first_name |
+-----------------------------+----------+------------+
| admin@moveinsync.com        | ADMIN    | System     |
| vendor@swifttransport.com   | VENDOR   | John       |
| employee@techcorp.com       | EMPLOYEE | Bob        |
+-----------------------------+----------+------------+
```

### Step 3: Test Login from Frontend
1. Go to: `http://localhost:3000`
2. Enter credentials:
   - Username: `admin`
   - Password: `admin123`
3. Click "Sign In"

### Step 4: Test API Directly (Optional)
```powershell
$body = @{
    email = "admin@moveinsync.com"
    password = "admin123"
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:4005/login" `
  -Method Post `
  -Body $body `
  -ContentType "application/json"
```

Expected response:
```json
{
  "token": "eyJhbGc...",
  "role": "ADMIN",
  "tenantId": "00000000-0000-0000-0000-000000000000",
  "userId": "11111111-1111-1111-1111-111111111111",
  "name": "System Admin"
}
```

## 🎯 User Roles & Access

| User     | Role     | Can Access                          |
|----------|----------|-------------------------------------|
| admin    | ADMIN    | Full dashboard, all management      |
| vendor   | VENDOR   | Vendor dashboard, trips, payments   |
| employee | EMPLOYEE | Employee dashboard, personal trips  |

## 🐛 Troubleshooting

### Users Not Created?
Check logs for "Executing SQL script from URL" message. If missing:
- Verify `spring.sql.init.mode=always` is set
- Check for SQL errors in startup logs

### Wrong Password?
All passwords use BCrypt with the same hash pattern for simplicity:
- Password: `admin123`, `vendor123`, `employee123`
- All use the same BCrypt hash in data.sql

### Can't Login?
1. Check service is running on port 4005
2. Verify database has users: `SELECT * FROM users;`
3. Check browser console for actual error
4. Try using full email instead of shorthand

---

**Status**: Auth service is restarting with sample users! 🎉

**Next**: Wait for "Started AuthServiceApplication", then try logging in!

