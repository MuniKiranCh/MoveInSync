#!/bin/bash

# ============================================
# Azure VM Setup Script for MoveInSync
# This script installs all dependencies needed
# ============================================

echo "=========================================="
echo "🚀 MoveInSync Azure VM Setup"
echo "=========================================="

# Update system packages
echo "📦 Updating system packages..."
sudo apt-get update -y
sudo apt-get upgrade -y

# Install essential tools
echo "🛠️ Installing essential tools..."
sudo apt-get install -y \
    apt-transport-https \
    ca-certificates \
    curl \
    gnupg \
    lsb-release \
    git \
    wget \
    unzip

# Install Docker
echo "🐳 Installing Docker..."
curl -fsSL https://get.docker.com -o get-docker.sh
sudo sh get-docker.sh
sudo usermod -aG docker $USER

# Install Docker Compose
echo "📦 Installing Docker Compose..."
sudo curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose

# Install Node.js and npm (for frontend)
echo "📦 Installing Node.js 18.x..."
curl -fsSL https://deb.nodesource.com/setup_18.x | sudo -E bash -
sudo apt-get install -y nodejs

# Verify installations
echo ""
echo "✅ Verifying installations..."
docker --version
docker-compose --version
node --version
npm --version

# Configure firewall (UFW)
echo ""
echo "🔒 Configuring firewall..."
sudo ufw --force enable
sudo ufw allow 22/tcp      # SSH
sudo ufw allow 80/tcp      # HTTP
sudo ufw allow 443/tcp     # HTTPS
sudo ufw allow 5173/tcp    # React Frontend
sudo ufw allow 4005/tcp    # Auth Service
sudo ufw allow 4010/tcp    # Client Service
sudo ufw allow 4015/tcp    # Vendor Service
sudo ufw allow 4020/tcp    # Trip Service
sudo ufw allow 4025/tcp    # Billing Service
sudo ufw allow 4035/tcp    # Employee Service
sudo ufw allow 3307/tcp    # MySQL (external port)
sudo ufw reload

# Create application directory
echo ""
echo "📁 Creating application directory..."
mkdir -p ~/moveinsync
cd ~/moveinsync

echo ""
echo "=========================================="
echo "✅ Setup Complete!"
echo "=========================================="
echo ""
echo "Next steps:"
echo "1. Upload your project files to ~/moveinsync/"
echo "2. Run: cd ~/moveinsync/backend && docker-compose up -d"
echo "3. Run: cd ~/moveinsync/frontend && npm install && npm run build"
echo "4. Your app will be accessible at: http://YOUR_AZURE_IP:5173"
echo ""
echo "⚠️ IMPORTANT: Log out and log back in for Docker permissions to take effect"
echo "   Run: exit"
echo "   Then reconnect to your VM"
echo ""

