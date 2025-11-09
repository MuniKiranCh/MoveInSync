# 🔍 Login Debugging Guide

## Issue: Page Reloads But Doesn't Login

This means the login request is failing. Let's debug step by step.

## Step 1: Check Browser Console

1. Open your browser at `http://localhost:3000`
2. Press **F12** to open Developer Tools
3. Go to **Console** tab
4. Try to login
5. Look for error messages (should be in red)

### Common Errors You Might See:

**Error 1: Connection Refused**
```
POST http://localhost:4005/login net::ERR_CONNECTION_REFUSED
```
**Fix**: Auth service is not running. Check the CMD window.

**Error 2: 401 Unauthorized**
```
POST http://localhost:4005/login 401 (Unauthorized)
```
**Fix**: Wrong credentials or users not in database.

**Error 3: JWT Decode Error**
```
Error: Invalid token specified
```
**Fix**: JWT structure doesn't match frontend expectations.

**Error 4: 400 Bad Request**
```
POST http://localhost:4005/login 400 (Bad Request)
```
**Fix**: Request format issue (likely email validation).

## Step 2: Check Network Tab

1. In DevTools, go to **Network** tab
2. Try to login again
3. Look for the `/login` request
4. Click on it
5. Check:
   - **Status**: Should be 200 (if successful)
   - **Request Payload**: Should show `{ email, password }`
   - **Response**: Should show `{ token, role, ... }`

## Step 3: Verify Auth Service is Running

Open the CMD window where auth-service is running. You should see:
```
✓ HikariPool-1 - Start completed
✓ Started AuthServiceApplication
✓ Tomcat started on port(s): 4005
```

If you see errors, the service didn't start properly.

## Step 4: Test API Directly

Open PowerShell and test:

```powershell
# Test 1: Is service responding?
curl http://localhost:4005/login

# Test 2: Try actual login
$body = @{
    email = "admin@moveinsync.com"
    password = "admin123"
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:4005/login" -Method Post -Body $body -ContentType "application/json"
```

### Expected Response:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIs...",
  "role": "ADMIN",
  "tenantId": "00000000-0000-0000-0000-000000000000",
  "userId": "11111111-1111-1111-1111-111111111111",
  "name": "System Admin"
}
```

## Step 5: Check Database

Verify users exist in database:

```powershell
mysql -u root -pQwerty@cs12345 -e "USE unified_billing_auth; SELECT email, role, first_name FROM users;"
```

Should show:
```
+-----------------------------+----------+------------+
| email                       | role     | first_name |
+-----------------------------+----------+------------+
| admin@moveinsync.com        | ADMIN    | System     |
| vendor@swifttransport.com   | VENDOR   | John       |
| employee@techcorp.com       | EMPLOYEE | Bob        |
+-----------------------------+----------+------------+
```

## Most Likely Issues:

### Issue A: Auth Service Not Started
**Symptom**: ERR_CONNECTION_REFUSED
**Fix**: Start the auth service

### Issue B: Users Not in Database
**Symptom**: 401 Unauthorized even with correct credentials
**Fix**: Check if data.sql ran. Look for "Executing SQL script" in startup logs.

### Issue C: JWT Token Structure Mismatch
**Symptom**: Login succeeds but immediate reload
**Fix**: Check JWT payload structure in LoginResponseDTO

### Issue D: Password Min Length Validation
**Symptom**: 400 Bad Request
**Fix**: Backend requires password to be at least 8 characters. All our passwords are 9+ chars, so this should be OK.

---

## 🎯 NEXT STEPS FOR YOU:

1. **Check browser console** (F12) for error messages
2. **Tell me what error you see** - this will help me fix it quickly
3. **Check if auth service CMD window shows "Started AuthServiceApplication"**

Please share:
- What error appears in browser console?
- Is auth service running (check CMD window)?
- What do you see in Network tab for the /login request?

