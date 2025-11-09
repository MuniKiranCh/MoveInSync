# Quick MySQL Connection Test

## Step 1: Start MySQL (Docker)
```bash
docker run --name mysql-unified-billing \
  -e MYSQL_ROOT_PASSWORD=root \
  -e MYSQL_DATABASE=unified_billing_auth \
  -p 3306:3306 \
  -d mysql:8.0
```

## Step 2: Wait for MySQL to be ready
```bash
# Check if MySQL is ready
docker logs mysql-unified-billing

# Or wait 30 seconds
sleep 30
```

## Step 3: Initialize Database (Optional - automatic with Spring Boot)
```bash
# Connect and run schema
docker exec -i mysql-unified-billing mysql -uroot -proot < src/main/resources/schema-mysql.sql
```

## Step 4: Update application.properties
Edit `src/main/resources/application.properties`:

**Comment these lines (H2 config):**
```properties
#spring.datasource.url=jdbc:h2:mem:authdb
#spring.datasource.driver-class-name=org.h2.Driver
#spring.datasource.username=admin
#spring.datasource.password=password
#spring.h2.console.enabled=true
#spring.h2.console.path=/h2-console
#spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
#spring.jpa.hibernate.ddl-auto=create-drop
#spring.sql.init.mode=always
```

**Uncomment these lines (MySQL config):**
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/unified_billing_auth?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.username=root
spring.datasource.password=root
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

## Step 5: Run the Auth Service
```bash
cd auth-service
mvn clean spring-boot:run
```

## Step 6: Test Login
```bash
# Test with Admin user
curl -X POST http://localhost:4005/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@moveinsync.com","password":"password123"}'

# Expected response with JWT token
```

## Step 7: Verify Data in MySQL
```bash
# Connect to MySQL
docker exec -it mysql-unified-billing mysql -uroot -proot unified_billing_auth

# Run queries
SELECT * FROM users;
SELECT email, role, first_name, last_name FROM users;
```

## Cleanup (Stop MySQL Container)
```bash
docker stop mysql-unified-billing
docker rm mysql-unified-billing
```

---

## Alternative: Local MySQL Installation

If you have MySQL installed locally:

1. **Start MySQL Service**
   - Windows: `net start MySQL80`
   - Linux: `sudo systemctl start mysql`
   - Mac: `mysql.server start`

2. **Create Database**
   ```bash
   mysql -u root -p
   CREATE DATABASE unified_billing_auth;
   exit;
   ```

3. **Update credentials in application.properties** if different from root/root

4. **Run application**
   ```bash
   mvn spring-boot:run
   ```

---

## Verification Checklist

- [ ] MySQL is running (Docker or local)
- [ ] Database `unified_billing_auth` exists
- [ ] `pom.xml` has `mysql-connector-j` dependency
- [ ] `application.properties` MySQL config is uncommented
- [ ] H2 config is commented out
- [ ] Service starts without errors
- [ ] Can login via `/login` endpoint
- [ ] Data persists after service restart

---

**✅ Ready to use MySQL with Auth Service!**



