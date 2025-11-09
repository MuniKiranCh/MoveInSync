@echo off
echo ========================================
echo Auth Service Startup Checker
echo ========================================
echo.

:CHECK
echo [%TIME%] Checking if auth-service is running on port 4005...

curl -s http://localhost:4005/login > nul 2>&1
if %errorlevel% equ 0 (
    echo.
    echo ========================================
    echo [SUCCESS] Auth Service is UP and RUNNING!
    echo ========================================
    echo.
    echo You can now:
    echo   1. Test login: curl -X POST http://localhost:4005/login -H "Content-Type: application/json" -d "{\"username\":\"admin\",\"password\":\"admin123\"}"
    echo   2. Refresh your browser at http://localhost:3000
    echo   3. Try logging in with admin/admin123
    echo.
    goto END
) else (
    echo [WAITING] Service not ready yet... (Will check again in 5 seconds)
    timeout /t 5 /nobreak > nul
    goto CHECK
)

:END
pause

