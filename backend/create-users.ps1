# Script to create initial users via API
# Wait for auth service to be ready
Write-Host "Waiting for Auth Service to be ready..."
Start-Sleep -Seconds 5

$authServiceUrl = "http://localhost:4005"

# 1. Create Super Admin
Write-Host "`nCreating Super Admin (admin@moveinsync.com)..."
$adminBody = @{
    email = "admin@moveinsync.com"
    password = "password"
    firstName = "System"
    lastName = "Admin"
    role = "ADMIN"
    tenantId = "00000000-0000-0000-0000-000000000000"
} | ConvertTo-Json

try {
    $response = Invoke-RestMethod -Uri "$authServiceUrl/create-user" -Method Post -Body $adminBody -ContentType "application/json"
    Write-Host "✓ Super Admin created successfully" -ForegroundColor Green
    Write-Host "  User ID: $($response.userId)"
} catch {
    if ($_.Exception.Response.StatusCode -eq 409) {
        Write-Host "⚠ Super Admin already exists" -ForegroundColor Yellow
    } else {
        Write-Host "✗ Failed to create Super Admin: $($_.Exception.Message)" -ForegroundColor Red
    }
}

# 2. Create Client (Amazon)
Write-Host "`nCreating Client Admin (admin@amazon.in)..."
$clientBody = @{
    email = "admin@amazon.in"
    password = "password"
    firstName = "Rahul"
    lastName = "Sharma"
    role = "CLIENT"
    tenantId = "a1111111-1111-1111-1111-111111111111"
} | ConvertTo-Json

try {
    $response = Invoke-RestMethod -Uri "$authServiceUrl/create-user" -Method Post -Body $clientBody -ContentType "application/json"
    Write-Host "✓ Client Admin created successfully" -ForegroundColor Green
    Write-Host "  User ID: $($response.userId)"
} catch {
    if ($_.Exception.Response.StatusCode -eq 409) {
        Write-Host "⚠ Client Admin already exists" -ForegroundColor Yellow
    } else {
        Write-Host "✗ Failed to create Client Admin: $($_.Exception.Message)" -ForegroundColor Red
    }
}

# 3. Create Vendor (Ola)
Write-Host "`nCreating Vendor User (vendor@ola.com)..."
$vendorBody = @{
    email = "vendor@ola.com"
    password = "password"
    firstName = "Amit"
    lastName = "Sharma"
    role = "VENDOR"
    tenantId = "11111111-1111-1111-1111-111111111111"
    vendorId = "11111111-1111-1111-1111-111111111111"
} | ConvertTo-Json

try {
    $response = Invoke-RestMethod -Uri "$authServiceUrl/create-user" -Method Post -Body $vendorBody -ContentType "application/json"
    Write-Host "✓ Vendor User created successfully" -ForegroundColor Green
    Write-Host "  User ID: $($response.userId)"
} catch {
    if ($_.Exception.Response.StatusCode -eq 409) {
        Write-Host "⚠ Vendor User already exists" -ForegroundColor Yellow
    } else {
        Write-Host "✗ Failed to create Vendor User: $($_.Exception.Message)" -ForegroundColor Red
    }
}

Write-Host "`n============================================"
Write-Host " User Creation Complete!"
Write-Host "============================================"
Write-Host "`nYou can now login with:"
Write-Host "  - Super Admin: admin@moveinsync.com / password"
Write-Host "  - Client: admin@amazon.in / password"
Write-Host "  - Vendor: vendor@ola.com / password"
Write-Host "`nPress any key to continue..."
$null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")

