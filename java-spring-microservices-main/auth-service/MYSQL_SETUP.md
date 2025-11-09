# MySQL Configuration for Auth Service

## 🔧 Setup Instructions

### 1. Install MySQL
- Download from: https://dev.mysql.com/downloads/mysql/
- Or use Docker: `docker run --name mysql-unified-billing -e MYSQL_ROOT_PASSWORD=root -p 3306:3306 -d mysql:8.0`

### 2. Create Database
```sql
CREATE DATABASE unified_billing_auth CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Optional: Create dedicated user
CREATE USER 'unified_billing'@'localhost' IDENTIFIED BY 'billing_password';
GRANT ALL PRIVILEGES ON unified_billing_auth.* TO 'unified_billing'@'localhost';
FLUSH PRIVILEGES;
```

### 3. Switch to MySQL

#### Option A: Edit application.properties
1. Open: `src/main/resources/application.properties`
2. **Comment out** H2 configuration (lines 14-27)
3. **Uncomment** MySQL configuration (lines 36-52)
4. Update credentials if needed:
   ```properties
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   ```

#### Option B: Use MySQL Profile
```bash
# Run with MySQL profile
mvn spring-boot:run -Dspring-boot.run.profiles=mysql

# Or set environment variable
export SPRING_PROFILES_ACTIVE=mysql
mvn spring-boot:run
```

### 4. Configuration Details

#### Current MySQL Configuration:
```properties
# Database URL (creates database automatically)
spring.datasource.url=jdbc:mysql://localhost:3306/unified_billing_auth?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true

# Driver
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# Credentials (CHANGE THESE!)
spring.datasource.username=root
spring.datasource.password=root

# Hibernate settings
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
spring.jpa.hibernate.ddl-auto=update  # Creates/updates tables automatically
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

#### Connection Pool Settings (HikariCP):
```properties
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.connection-timeout=30000
spring.datasource.hikari.idle-timeout=600000
spring.datasource.hikari.max-lifetime=1800000
```

### 5. Initialize Sample Data

The `data.sql` file will automatically execute on first run when using `ddl-auto=update`.

If you need to manually insert data:

```sql
USE unified_billing_auth;

-- Insert sample users (password: password123)
INSERT INTO users (id, email, password, role, tenant_id, first_name, last_name, active, created_at, updated_at)
VALUES 
('11111111-1111-1111-1111-111111111111', 'admin@moveinsync.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ADMIN', '00000000-0000-0000-0000-000000000000', 'System', 'Admin', true, NOW(), NOW()),
('22222222-2222-2222-2222-222222222222', 'manager@techcorp.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'CLIENT_MANAGER', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'Alice', 'Manager', true, NOW(), NOW()),
('33333333-3333-3333-3333-333333333333', 'vendor@swifttransport.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'VENDOR', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'John', 'Vendor', true, NOW(), NOW()),
('44444444-4444-4444-4444-444444444444', 'employee@techcorp.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'EMPLOYEE', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'Bob', 'Employee', true, NOW(), NOW()),
('55555555-5555-5555-5555-555555555555', 'finance@moveinsync.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'FINANCE_TEAM', '00000000-0000-0000-0000-000000000000', 'Sarah', 'Finance', true, NOW(), NOW());
```

### 6. Verify Connection

```bash
# Start the service
cd auth-service
mvn spring-boot:run

# Test login
curl -X POST http://localhost:4005/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@moveinsync.com","password":"password123"}'
```

### 7. View Data in MySQL

```bash
# Connect to MySQL
mysql -u root -p

# Use database
USE unified_billing_auth;

# View users
SELECT id, email, role, tenant_id, first_name, last_name, active FROM users;
```

## 🔍 Troubleshooting

### Connection Refused
- Ensure MySQL is running: `sudo systemctl status mysql` (Linux) or check Services (Windows)
- Verify port: `netstat -an | grep 3306`
- Check firewall settings

### Authentication Failed
- Update credentials in `application.properties`
- Ensure user has proper permissions
- Try: `GRANT ALL PRIVILEGES ON *.* TO 'root'@'localhost' WITH GRANT OPTION;`

### Database Not Created
- Check `createDatabaseIfNotExist=true` is in connection URL
- Manually create database: `CREATE DATABASE unified_billing_auth;`

### Sample Data Not Loading
- For MySQL, `data.sql` might not auto-execute with `ddl-auto=update`
- Manually run the SQL insert statements above
- Or change to: `spring.jpa.hibernate.ddl-auto=create` (WARNING: Drops tables on restart!)

## 📋 Configuration Comparison

| Feature | H2 (Dev) | MySQL (Prod) |
|---------|----------|--------------|
| **Database** | In-memory | Persistent |
| **Data Persistence** | Lost on restart | Permanent |
| **Console** | http://localhost:4005/h2-console | MySQL Workbench/CLI |
| **Performance** | Very fast | Production-grade |
| **Use Case** | Development/Testing | Production |

## 🚀 Quick Switch Commands

### Switch to MySQL:
1. Edit `application.properties` - uncomment MySQL section
2. Run: `mvn spring-boot:run`

### Switch back to H2:
1. Edit `application.properties` - uncomment H2 section
2. Run: `mvn spring-boot:run`

## 📊 Database Schema

The Auth Service uses the following table:

```sql
CREATE TABLE users (
    id CHAR(36) PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    tenant_id CHAR(36) NOT NULL,
    vendor_id CHAR(36),
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_tenant (tenant_id),
    INDEX idx_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
```

## 🔐 Security Best Practices

1. **Never use default passwords in production**
2. **Use environment variables for credentials**:
   ```bash
   export DB_USERNAME=your_username
   export DB_PASSWORD=your_password
   ```
   
   Then in `application.properties`:
   ```properties
   spring.datasource.username=${DB_USERNAME}
   spring.datasource.password=${DB_PASSWORD}
   ```

3. **Enable SSL for production**:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/unified_billing_auth?useSSL=true&requireSSL=true
   ```

4. **Use connection pooling** (HikariCP is default and configured)

5. **Regular backups**:
   ```bash
   mysqldump -u root -p unified_billing_auth > backup.sql
   ```

---

**✅ MySQL Configuration Complete!**

Your Auth Service now supports both H2 (development) and MySQL (production) databases.

