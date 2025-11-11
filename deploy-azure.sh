#!/bin/bash

# ============================================
# MoveInSync Deployment Script for Azure VM
# Run this script on your Azure VM after setup
# ============================================

echo "=========================================="
echo "🚀 Deploying MoveInSync to Azure"
echo "=========================================="

# Check if running as root
if [ "$EUID" -eq 0 ]; then 
    echo "❌ Please do not run as root. Run as your regular user."
    exit 1
fi

# Check if Docker is installed
if ! command -v docker &> /dev/null; then
    echo "❌ Docker is not installed. Please run azure-setup.sh first."
    exit 1
fi

# Get Azure VM IP address
echo ""
echo "📡 Detecting Azure VM IP address..."
AZURE_IP=$(curl -s ifconfig.me || curl -s icanhazip.com || curl -s ipecho.net/plain)

if [ -z "$AZURE_IP" ]; then
    echo "❌ Could not detect Azure IP. Please enter it manually:"
    read -p "Enter your Azure VM public IP: " AZURE_IP
fi

echo "✅ Azure VM IP: $AZURE_IP"

# Navigate to project directory
cd ~/moveinsync || {
    echo "❌ Project directory not found. Please upload your project to ~/moveinsync/"
    exit 1
}

# ============================================
# Step 1: Deploy Backend Services
# ============================================
echo ""
echo "=========================================="
echo "📦 Step 1: Deploying Backend Services"
echo "=========================================="

cd backend

# Stop any existing containers
echo "🛑 Stopping existing containers..."
docker-compose -f docker-compose.prod.yml down 2>/dev/null || true

# Build and start services
echo "🏗️ Building and starting services..."
docker-compose -f docker-compose.prod.yml up --build -d

# Wait for services to be healthy
echo "⏳ Waiting for services to start (this may take 2-3 minutes)..."
sleep 60

# Check service status
echo ""
echo "📊 Service Status:"
docker-compose -f docker-compose.prod.yml ps

# ============================================
# Step 2: Deploy Frontend
# ============================================
echo ""
echo "=========================================="
echo "📦 Step 2: Deploying Frontend"
echo "=========================================="

cd ~/moveinsync/frontend

# Create .env file with Azure IP
echo "📝 Configuring frontend for Azure IP: $AZURE_IP"
cat > .env.production << EOF
VITE_API_BASE_URL=http://$AZURE_IP
VITE_AUTH_SERVICE_URL=http://$AZURE_IP:4005
VITE_CLIENT_SERVICE_URL=http://$AZURE_IP:4010
VITE_VENDOR_SERVICE_URL=http://$AZURE_IP:4015
VITE_TRIP_SERVICE_URL=http://$AZURE_IP:4020
VITE_BILLING_SERVICE_URL=http://$AZURE_IP:4025
VITE_EMPLOYEE_SERVICE_URL=http://$AZURE_IP:4035
EOF

# Install dependencies
echo "📦 Installing frontend dependencies..."
npm install

# Build frontend
echo "🏗️ Building frontend..."
npm run build

# Install PM2 for running frontend
if ! command -v pm2 &> /dev/null; then
    echo "📦 Installing PM2..."
    sudo npm install -g pm2
fi

# Stop any existing PM2 process
pm2 delete moveinsync-frontend 2>/dev/null || true

# Start frontend with PM2
echo "🚀 Starting frontend server..."
pm2 serve dist 5173 --name moveinsync-frontend --spa
pm2 save
pm2 startup | tail -n 1 | sudo bash

# ============================================
# Step 3: Health Checks
# ============================================
echo ""
echo "=========================================="
echo "🏥 Step 3: Health Checks"
echo "=========================================="

sleep 10

echo "Checking services..."
services=("4005:Auth" "4010:Client" "4015:Vendor" "4020:Trip" "4025:Billing" "4035:Employee")

for service in "${services[@]}"; do
    IFS=':' read -r port name <<< "$service"
    if curl -s "http://localhost:$port" > /dev/null 2>&1; then
        echo "✅ $name Service (port $port): Running"
    else
        echo "⚠️ $name Service (port $port): Not responding (may still be starting)"
    fi
done

# ============================================
# Deployment Complete
# ============================================
echo ""
echo "=========================================="
echo "✅ Deployment Complete!"
echo "=========================================="
echo ""
echo "🌐 Your application is now accessible at:"
echo ""
echo "   Frontend:  http://$AZURE_IP:5173"
echo ""
echo "   Backend Services:"
echo "   - Auth Service:     http://$AZURE_IP:4005"
echo "   - Client Service:   http://$AZURE_IP:4010"
echo "   - Vendor Service:   http://$AZURE_IP:4015"
echo "   - Trip Service:     http://$AZURE_IP:4020"
echo "   - Billing Service:  http://$AZURE_IP:4025"
echo "   - Employee Service: http://$AZURE_IP:4035"
echo ""
echo "📋 Useful Commands:"
echo "   - View backend logs:     cd ~/moveinsync/backend && docker-compose -f docker-compose.prod.yml logs -f"
echo "   - View frontend logs:    pm2 logs moveinsync-frontend"
echo "   - Restart backend:       cd ~/moveinsync/backend && docker-compose -f docker-compose.prod.yml restart"
echo "   - Restart frontend:      pm2 restart moveinsync-frontend"
echo "   - View all containers:   docker ps"
echo "   - View PM2 processes:    pm2 status"
echo ""
echo "🔐 Test Credentials:"
echo "   Email:    admin@moveinsync.com"
echo "   Password: password"
echo ""
echo "⚠️ SECURITY NOTE: Change default passwords before production use!"
echo ""

