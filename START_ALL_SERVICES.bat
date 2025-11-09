@echo off
echo ========================================
echo  Starting All MoveInSync Services
echo ========================================
echo.

echo Starting Auth Service (Port 4005)...
start "Auth Service - 4005" cmd /k "cd /d C:\Users\munik\OneDrive\Desktop\MoveInSync\java-spring-microservices-main\auth-service && echo Auth Service - Port 4005 && mvn spring-boot:run"
timeout /t 2 /nobreak >nul

echo Starting Client Service (Port 4010)...
start "Client Service - 4010" cmd /k "cd /d C:\Users\munik\OneDrive\Desktop\MoveInSync\java-spring-microservices-main\client-service && echo Client Service - Port 4010 && mvn spring-boot:run"
timeout /t 2 /nobreak >nul

echo Starting Vendor Service (Port 4015)...
start "Vendor Service - 4015" cmd /k "cd /d C:\Users\munik\OneDrive\Desktop\MoveInSync\java-spring-microservices-main\vendor-service && echo Vendor Service - Port 4015 && mvn spring-boot:run"
timeout /t 2 /nobreak >nul

echo Starting Trip Service (Port 4020)...
start "Trip Service - 4020" cmd /k "cd /d C:\Users\munik\OneDrive\Desktop\MoveInSync\java-spring-microservices-main\trip-service && echo Trip Service - Port 4020 && mvn spring-boot:run"
timeout /t 2 /nobreak >nul

echo Starting Billing Service (Port 4025)...
start "Billing Service - 4025" cmd /k "cd /d C:\Users\munik\OneDrive\Desktop\MoveInSync\java-spring-microservices-main\billing-service && echo Billing Service - Port 4025 && mvn spring-boot:run"
timeout /t 2 /nobreak >nul

echo Starting Analytics Service (Port 4030)...
start "Analytics Service - 4030" cmd /k "cd /d C:\Users\munik\OneDrive\Desktop\MoveInSync\java-spring-microservices-main\analytics-service && echo Analytics Service - Port 4030 && mvn spring-boot:run"

echo.
echo ========================================
echo All services are starting!
echo Wait 60-90 seconds for them to complete
echo ========================================
echo.
echo You should see 6 command windows opening
echo Look for "Started [Service]Application" in each window
echo.
pause

