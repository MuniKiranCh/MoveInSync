# 🚀 Azure Deployment Guide for MoveInSync

Complete step-by-step guide to deploy MoveInSync on Azure with a public IP address.

---

## 📋 Table of Contents

1. [Prerequisites](#prerequisites)
2. [Create Azure Virtual Machine](#create-azure-virtual-machine)
3. [Upload Project Files](#upload-project-files)
4. [Run Setup Script](#run-setup-script)
5. [Deploy Application](#deploy-application)
6. [Access Your Application](#access-your-application)
7. [Troubleshooting](#troubleshooting)
8. [Management Commands](#management-commands)

---

## 1️⃣ Prerequisites

Before starting, you need:

- ✅ **Azure Account** ([Sign up for free](https://azure.microsoft.com/free/))
- ✅ **SSH Client** (PuTTY for Windows, or Terminal for Mac/Linux)
- ✅ **FileZilla or SCP** for file transfer (optional, we'll use Git)
- ✅ Your project pushed to GitHub

---

## 2️⃣ Create Azure Virtual Machine

### Step 2.1: Create VM in Azure Portal

1. **Go to Azure Portal**: https://portal.azure.com
2. Click **"Create a resource"** → **"Virtual Machine"**
3. Configure the VM:

   **Basics Tab:**
   - **Subscription**: Select your subscription
   - **Resource Group**: Create new → `moveinsync-rg`
   - **Virtual Machine Name**: `moveinsync-vm`
   - **Region**: Choose closest to you (e.g., `East US`)
   - **Image**: **Ubuntu Server 22.04 LTS**
   - **Size**: **Standard_B2s** (2 vCPUs, 4 GB RAM) - Minimum recommended
     - Or **Standard_B2ms** (2 vCPUs, 8 GB RAM) - Better performance
   - **Authentication type**: SSH public key or Password
   - **Username**: `azureuser` (or your preferred name)
   - **Password/SSH Key**: Create a strong password or generate SSH key

   **Disks Tab:**
   - **OS disk type**: Standard SSD (30 GB is enough)

   **Networking Tab:**
   - **Virtual network**: Create new (default settings)
   - **Subnet**: default
   - **Public IP**: Create new
   - **NIC network security group**: Basic
   - **Public inbound ports**: Select **SSH (22)**, **HTTP (80)**, **HTTPS (443)**

4. Click **"Review + Create"** → **"Create"**
5. Wait 2-3 minutes for deployment to complete

### Step 2.2: Configure Network Security Group (Firewall)

1. Go to your VM → **Networking** → **Network security group**
2. Click **"Add inbound port rule"** and add these rules:

| Priority | Port | Protocol | Source | Name |
|----------|------|----------|--------|------|
| 310 | 5173 | TCP | * | Frontend |
| 320 | 4005 | TCP | * | AuthService |
| 330 | 4010 | TCP | * | ClientService |
| 340 | 4015 | TCP | * | VendorService |
| 350 | 4020 | TCP | * | TripService |
| 360 | 4025 | TCP | * | BillingService |
| 370 | 4035 | TCP | * | EmployeeService |
| 380 | 3307 | TCP | * | MySQL |

3. Click **Save** after adding each rule

### Step 2.3: Get Your Public IP Address

1. Go to your VM → **Overview**
2. Copy the **Public IP address** (e.g., `20.185.45.123`)
3. **Save this IP** - you'll need it throughout

---

## 3️⃣ Upload Project Files

### Option A: Using Git (Recommended)

1. **SSH into your VM:**
   ```bash
   ssh azureuser@YOUR_AZURE_IP
   # Enter password when prompted
   ```

2. **Clone your repository:**
   ```bash
   cd ~
   git clone https://github.com/YOUR_USERNAME/MoveInSync.git
   mv MoveInSync moveinsync
   cd moveinsync
   ```

3. **Verify files are there:**
   ```bash
   ls -la
   # You should see: backend/, frontend/, azure-setup.sh, deploy-azure.sh, etc.
   ```

### Option B: Using FileZilla/SCP

1. **Install FileZilla**: https://filezilla-project.org/

2. **Connect to your VM:**
   - Host: `sftp://YOUR_AZURE_IP`
   - Username: `azureuser`
   - Password: Your VM password
   - Port: 22

3. **Upload the entire project folder to** `/home/azureuser/moveinsync/`

---

## 4️⃣ Run Setup Script

This installs Docker, Docker Compose, Node.js, and configures the firewall.

```bash
# Make sure you're in the project directory
cd ~/moveinsync

# Make scripts executable
chmod +x azure-setup.sh
chmod +x deploy-azure.sh

# Run setup script
./azure-setup.sh

# Wait for completion (5-10 minutes)
```

**Important:** After setup completes, you MUST log out and log back in:

```bash
exit
```

Then reconnect:

```bash
ssh azureuser@YOUR_AZURE_IP
cd ~/moveinsync
```

---

## 5️⃣ Deploy Application

Now deploy the backend and frontend:

```bash
cd ~/moveinsync

# Run deployment script
./deploy-azure.sh
```

The script will:
- ✅ Auto-detect your Azure IP
- ✅ Build and start all backend services with Docker
- ✅ Configure frontend with your Azure IP
- ✅ Build and start the frontend
- ✅ Set up auto-restart on reboot

**Wait 3-5 minutes** for all services to start and initialize databases.

---

## 6️⃣ Access Your Application

### Frontend (User Interface)
```
http://YOUR_AZURE_IP:5173
```

### Backend Services (APIs)
```
Auth Service:     http://YOUR_AZURE_IP:4005
Client Service:   http://YOUR_AZURE_IP:4010
Vendor Service:   http://YOUR_AZURE_IP:4015
Trip Service:     http://YOUR_AZURE_IP:4020
Billing Service:  http://YOUR_AZURE_IP:4025
Employee Service: http://YOUR_AZURE_IP:4035
```

### Test Login Credentials
```
Email:    admin@moveinsync.com
Password: password
```

---

## 7️⃣ Troubleshooting

### Services Not Starting

**Check backend logs:**
```bash
cd ~/moveinsync/backend
docker-compose -f docker-compose.prod.yml logs -f
# Press Ctrl+C to exit
```

**Check specific service:**
```bash
docker logs moveinsync-auth-service
docker logs moveinsync-mysql
```

**Restart all services:**
```bash
cd ~/moveinsync/backend
docker-compose -f docker-compose.prod.yml restart
```

### Frontend Not Loading

**Check frontend logs:**
```bash
pm2 logs moveinsync-frontend
```

**Restart frontend:**
```bash
pm2 restart moveinsync-frontend
```

**Rebuild frontend:**
```bash
cd ~/moveinsync/frontend
npm run build
pm2 restart moveinsync-frontend
```

### Cannot Access from Browser

**Check firewall:**
```bash
sudo ufw status
# Should show all ports as ALLOW
```

**Check Azure NSG (Network Security Group):**
- Go to Azure Portal → Your VM → Networking
- Ensure all inbound port rules are added (step 2.2)

**Check services are running:**
```bash
docker ps
pm2 status
```

### Database Errors

**Restart MySQL:**
```bash
cd ~/moveinsync/backend
docker-compose -f docker-compose.prod.yml restart mysql
```

**View MySQL logs:**
```bash
docker logs moveinsync-mysql
```

---

## 8️⃣ Management Commands

### Backend Services (Docker)

```bash
# Navigate to backend directory
cd ~/moveinsync/backend

# View all running containers
docker ps

# View logs for all services
docker-compose -f docker-compose.prod.yml logs -f

# View logs for specific service
docker logs moveinsync-auth-service -f

# Restart all services
docker-compose -f docker-compose.prod.yml restart

# Restart specific service
docker-compose -f docker-compose.prod.yml restart auth-service

# Stop all services
docker-compose -f docker-compose.prod.yml down

# Start all services
docker-compose -f docker-compose.prod.yml up -d

# Rebuild and restart
docker-compose -f docker-compose.prod.yml up --build -d
```

### Frontend (PM2)

```bash
# View PM2 processes
pm2 status

# View frontend logs
pm2 logs moveinsync-frontend

# Restart frontend
pm2 restart moveinsync-frontend

# Stop frontend
pm2 stop moveinsync-frontend

# Start frontend
pm2 start moveinsync-frontend

# View detailed info
pm2 info moveinsync-frontend
```

### System Management

```bash
# Check disk space
df -h

# Check memory usage
free -h

# Check CPU usage
top
# Press 'q' to exit

# Check port usage
sudo netstat -tulpn | grep LISTEN

# Restart VM (from Azure Portal or CLI)
sudo reboot
```

---

## 🔒 Security Recommendations

### Before Going to Production:

1. **Change Default Passwords:**
   - Update MySQL password in `backend/docker-compose.prod.yml`
   - Change default user passwords in database

2. **Enable HTTPS:**
   - Get SSL certificate (Let's Encrypt free)
   - Configure Nginx reverse proxy
   - Update frontend URLs to HTTPS

3. **Restrict Database Access:**
   - Remove MySQL port 3307 from public access
   - Only allow backend services to connect

4. **Enable Azure Monitoring:**
   - Set up Azure Monitor
   - Configure alerts for high CPU/memory
   - Enable diagnostic logging

5. **Regular Updates:**
   ```bash
   sudo apt update && sudo apt upgrade -y
   ```

---

## 💰 Cost Estimation

**Azure VM (Standard_B2s):**
- ~$30-40/month (2 vCPUs, 4 GB RAM)
- Pay-as-you-go pricing

**Cost Optimization:**
- Stop VM when not in use (for development)
- Use Reserved Instances for production (save up to 72%)
- Monitor resources and scale down if not needed

---

## 📞 Support

If you encounter issues:

1. Check logs: `docker-compose logs` and `pm2 logs`
2. Verify all ports are open in Azure NSG
3. Ensure services are running: `docker ps` and `pm2 status`
4. Check Azure VM has enough resources (CPU, memory, disk)

---

## ✅ Success Checklist

- [ ] Azure VM created with Ubuntu 22.04
- [ ] Network Security Group configured with all ports
- [ ] Project files uploaded to VM
- [ ] Setup script executed successfully
- [ ] Logged out and back in after setup
- [ ] Deployment script executed successfully
- [ ] All Docker containers running (`docker ps`)
- [ ] Frontend PM2 process running (`pm2 status`)
- [ ] Can access frontend at `http://YOUR_IP:5173`
- [ ] Can login with test credentials
- [ ] All backend services responding

---

**🎉 Congratulations! Your MoveInSync application is now live on Azure!**

Access it at: `http://YOUR_AZURE_IP:5173`

