# Auth Service - Database Connection Verification Guide

## Current Configuration

**Database**: MySQL
**URL**: `jdbc:mysql://localhost:3306/unified_billing_auth`
**Username**: `root`
**Password**: `root`
**Port**: `4005` (Auth Service)

## How to Verify Database Connection

### Option 1: Check Application Logs

When you run the auth-service, look for these log messages:

#### ✅ Successful Connection Indicators:
```
HikariPool-1 - Starting...
HikariPool-1 - Start completed
HikariCP connection pool initialized
Initialized JPA EntityManagerFactory for persistence unit 'default'
Started AuthServiceApplication in X.XXX seconds
```

#### ❌ Connection Failure Indicators:
```
Failed to initialize pool: Access denied
Cannot create PoolableConnectionFactory
Communications link failure
```

### Option 2: Check Database Directly

Run these MySQL commands:

```bash
# 1. Connect to MySQL
mysql -u root -proot

# 2. Check if database exists
SHOW DATABASES LIKE 'unified_billing_auth';

# 3. Use the database
USE unified_billing_auth;

# 4. Show tables
SHOW TABLES;

# 5. Check users table
SELECT * FROM users;
```

### Option 3: Use the Verification Script

Run the provided batch script:
```bash
cd java-spring-microservices-main/auth-service
./check-db-connection.bat
```

### Option 4: Test API Endpoint

Once the service is running, test the health endpoint:

```bash
# Check if service is running
curl http://localhost:4005/actuator/health

# Or test login endpoint
curl -X POST http://localhost:4005/auth/login -H "Content-Type: application/json" -d "{\"username\":\"admin\",\"password\":\"admin123\"}"
```

## Common Issues and Solutions

### Issue 1: Access Denied
**Error**: `Access denied for user 'root'@'localhost'`

**Solution**:
1. Update password in `application.properties`
2. Or reset MySQL root password:
   ```sql
   ALTER USER 'root'@'localhost' IDENTIFIED BY 'root';
   FLUSH PRIVILEGES;
   ```

### Issue 2: Database Not Created
**Error**: `Unknown database 'unified_billing_auth'`

**Solution**: The database should auto-create due to `createDatabaseIfNotExist=true` in the connection URL. If not, create manually:
```sql
CREATE DATABASE IF NOT EXISTS unified_billing_auth;
```

### Issue 3: Port Already in Use
**Error**: `Port 4005 is already in use`

**Solution**:
1. Find and kill the process using port 4005
2. Or change the port in `application.properties`

### Issue 4: MySQL Service Not Running
**Error**: `Communications link failure`

**Solution**:
```powershell
# Start MySQL service
net start MySQL80

# Or check service status
Get-Service -Name MySQL*
```

## Database Schema

After successful connection, these tables will be created:

```
users
  - id (bigint, primary key)
  - username (varchar)
  - password (varchar, hashed)
  - email (varchar)
  - role (varchar)
  - created_at (timestamp)
  - updated_at (timestamp)
```

## What to Look For in Logs

### Startup Sequence (Successful):

1. **Banner Display** ✓
2. **Database URL**: `jdbc:mysql://localhost:3306/unified_billing_auth`
3. **HikariCP Starting**: Connection pool initialization
4. **HikariPool-1 - Start completed**: Pool ready ✅
5. **Hibernate Initialization**: JPA setup
6. **Schema Creation/Update**: DDL operations
7. **Sample Data Loaded**: Initial users created (from data.sql)
8. **Tomcat Started**: Port 4005 ✅
9. **Application Started**: Service ready ✅

### Example Success Log:
```
2024-01-20 10:30:15.123  INFO --- [main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Starting...
2024-01-20 10:30:15.456  INFO --- [main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Start completed.
2024-01-20 10:30:16.789  INFO --- [main] o.hibernate.jpa.internal.util.LogHelper  : HHH000204: Processing PersistenceUnitInfo [name: default]
2024-01-20 10:30:17.012  INFO --- [main] org.hibernate.Version                    : HHH000412: Hibernate ORM core version 6.4.1.Final
2024-01-20 10:30:18.234  INFO --- [main] o.h.e.t.j.p.i.JtaPlatformInitiator       : HHH000490: Using JtaPlatform implementation: [org.hibernate.engine.transaction.jta.platform.internal.NoJtaPlatform]
2024-01-20 10:30:18.567  INFO --- [main] j.LocalContainerEntityManagerFactoryBean : Initialized JPA EntityManagerFactory for persistence unit 'default'
2024-01-20 10:30:20.890  INFO --- [main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 4005 (http)
2024-01-20 10:30:21.123  INFO --- [main] com.pm.authservice.AuthServiceApplication : Started AuthServiceApplication in 8.456 seconds
```

## Testing the Connection

Once the service is up, you can verify it's working:

```bash
# Test 1: Check service health
curl http://localhost:4005/actuator/health

# Test 2: Try to login
curl -X POST http://localhost:4005/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'

# Expected response: JWT token
```

## Monitoring Database Activity

To see real-time database queries, the service is configured with:
- `spring.jpa.show-sql=true` - Shows SQL statements
- `spring.jpa.properties.hibernate.format_sql=true` - Formats SQL for readability
- `logging.level.org.hibernate.SQL=debug` - Detailed SQL logging

You'll see queries like:
```sql
Hibernate: select u1_0.id,u1_0.email,u1_0.password,u1_0.role,u1_0.username from users u1_0 where u1_0.username=?
```

---

**Status**: MySQL service is running ✅
**Next Step**: Wait for application startup and check the logs above

