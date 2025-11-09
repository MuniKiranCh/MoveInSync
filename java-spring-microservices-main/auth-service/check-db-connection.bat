@echo off
echo ========================================
echo Auth Service - Database Connection Test
echo ========================================
echo.

echo Testing MySQL Connection...
mysql -u root -proot -e "SHOW DATABASES LIKE 'unified_billing_auth';" 2>nul
if %errorlevel% equ 0 (
    echo [SUCCESS] MySQL is accessible
    echo.
    echo Checking if database exists...
    mysql -u root -proot -e "USE unified_billing_auth; SHOW TABLES;" 2>nul
    if %errorlevel% equ 0 (
        echo [SUCCESS] Database 'unified_billing_auth' exists
        echo.
        echo Tables in database:
        mysql -u root -proot -D unified_billing_auth -e "SHOW TABLES;"
        echo.
        echo Checking users table:
        mysql -u root -proot -D unified_billing_auth -e "SELECT COUNT(*) as user_count FROM users;" 2>nul
        if %errorlevel% equ 0 (
            echo [SUCCESS] Users table exists and is accessible
        ) else (
            echo [INFO] Users table not yet created - will be created on first run
        )
    ) else (
        echo [INFO] Database will be auto-created by Spring Boot
    )
) else (
    echo [ERROR] Cannot connect to MySQL. Please check:
    echo   1. MySQL service is running
    echo   2. Username and password are correct (root/root)
    echo   3. MySQL is running on port 3306
)

echo.
echo ========================================
echo.
echo To check if auth-service connected successfully, look for these logs:
echo   - "HikariPool-1 - Starting..."
echo   - "HikariPool-1 - Start completed"
echo   - "Initialized JPA EntityManagerFactory"
echo   - "Started AuthServiceApplication"
echo.
pause

