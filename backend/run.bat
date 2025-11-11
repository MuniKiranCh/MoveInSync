@echo off
echo ============================================
echo  MoveInSync Backend Services Manager
echo ============================================
echo.

echo Stopping existing services on ports...
echo.

REM Function to kill process on a specific port
REM Kill Auth Service on Port 4005
echo Checking port 4005 (Auth Service)...
for /f "tokens=5" %%a in ('netstat -aon ^| findstr :4005 ^| findstr LISTENING') do (
    echo Killing process %%a on port 4005
    taskkill /F /PID %%a 2>nul
)

REM Kill Client Service on Port 4010
echo Checking port 4010 (Client Service)...
for /f "tokens=5" %%a in ('netstat -aon ^| findstr :4010 ^| findstr LISTENING') do (
    echo Killing process %%a on port 4010
    taskkill /F /PID %%a 2>nul
)

REM Kill Vendor Service on Port 4015
echo Checking port 4015 (Vendor Service)...
for /f "tokens=5" %%a in ('netstat -aon ^| findstr :4015 ^| findstr LISTENING') do (
    echo Killing process %%a on port 4015
    taskkill /F /PID %%a 2>nul
)

REM Kill Trip Service on Port 4020
echo Checking port 4020 (Trip Service)...
for /f "tokens=5" %%a in ('netstat -aon ^| findstr :4020 ^| findstr LISTENING') do (
    echo Killing process %%a on port 4020
    taskkill /F /PID %%a 2>nul
)

REM Kill Billing Service on Port 4025
echo Checking port 4025 (Billing Service)...
for /f "tokens=5" %%a in ('netstat -aon ^| findstr :4025 ^| findstr LISTENING') do (
    echo Killing process %%a on port 4025
    taskkill /F /PID %%a 2>nul
)

REM Kill Employee Service on Port 4035
echo Checking port 4035 (Employee Service)...
for /f "tokens=5" %%a in ('netstat -aon ^| findstr :4035 ^| findstr LISTENING') do (
    echo Killing process %%a on port 4035
    taskkill /F /PID %%a 2>nul
)

echo.
echo All existing services stopped.
echo.
echo Starting new service instances...
echo.

REM Start Auth Service (Port 4005)
echo Starting Auth Service on port 4005...
start "Auth Service - Port 4005" cmd /k "cd auth-service && mvnw.cmd spring-boot:run"
timeout /t 2 /nobreak >nul

REM Start Client Service (Port 4010)
echo Starting Client Service on port 4010...
start "Client Service - Port 4010" cmd /k "cd client-service && mvn spring-boot:run"
timeout /t 2 /nobreak >nul

REM Start Vendor Service (Port 4015)
echo Starting Vendor Service on port 4015...
start "Vendor Service - Port 4015" cmd /k "cd vendor-service && mvn spring-boot:run"
timeout /t 2 /nobreak >nul

REM Start Trip Service (Port 4020)
echo Starting Trip Service on port 4020...
start "Trip Service - Port 4020" cmd /k "cd trip-service && mvn spring-boot:run"
timeout /t 2 /nobreak >nul

REM Start Billing Service (Port 4025)
echo Starting Billing Service on port 4025...
start "Billing Service - Port 4025" cmd /k "cd billing-service && mvn spring-boot:run"
timeout /t 2 /nobreak >nul

REM Start Employee Service (Port 4035)
echo Starting Employee Service on port 4035...
start "Employee Service - Port 4035" cmd /k "cd employee-service && mvn spring-boot:run"

echo.
echo ============================================
echo  All services are starting...
echo  Check individual windows for status.
echo ============================================
echo.
echo Service Ports:
echo   - Auth Service:     http://localhost:4005
echo   - Client Service:   http://localhost:4010
echo   - Vendor Service:   http://localhost:4015
echo   - Trip Service:     http://localhost:4020
echo   - Billing Service:  http://localhost:4025
echo   - Employee Service: http://localhost:4035
echo.
pause
