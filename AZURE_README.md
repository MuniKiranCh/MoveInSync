# MoveInSync Azure Deployment Files

This folder contains all scripts and configurations needed to deploy MoveInSync on Azure.

## 📁 Files Overview

| File | Description |
|------|-------------|
| `azure-setup.sh` | Initial VM setup - installs Docker, Node.js, and configures firewall |
| `deploy-azure.sh` | Main deployment script - deploys backend and frontend |
| `docker-compose.prod.yml` | Production Docker Compose configuration |
| `AZURE_DEPLOYMENT_GUIDE.md` | Complete step-by-step deployment guide |
| `AZURE_QUICK_CHECKLIST.md` | Quick reference checklist |

## 🚀 Quick Start

### 1. Create Azure VM
- Ubuntu 22.04 LTS
- Standard_B2s (2 vCPUs, 4 GB RAM minimum)
- Configure Network Security Group with required ports

### 2. Upload Project
```bash
ssh azureuser@YOUR_AZURE_IP
cd ~
git clone https://github.com/YOUR_USERNAME/MoveInSync.git
mv MoveInSync moveinsync
cd moveinsync
```

### 3. Run Setup
```bash
chmod +x azure-setup.sh deploy-azure.sh
./azure-setup.sh
# Log out and back in
exit
ssh azureuser@YOUR_AZURE_IP
```

### 4. Deploy
```bash
cd ~/moveinsync
./deploy-azure.sh
```

### 5. Access Application
```
http://YOUR_AZURE_IP:5173
```

## 📚 Documentation

For detailed instructions, see **[AZURE_DEPLOYMENT_GUIDE.md](./AZURE_DEPLOYMENT_GUIDE.md)**

For quick reference, see **[AZURE_QUICK_CHECKLIST.md](./AZURE_QUICK_CHECKLIST.md)**

## 🔧 Required Azure Ports

| Port | Service | Required |
|------|---------|----------|
| 22 | SSH | ✅ Yes |
| 80 | HTTP | ✅ Yes |
| 443 | HTTPS | ⚠️ For production |
| 5173 | Frontend | ✅ Yes |
| 4005 | Auth Service | ✅ Yes |
| 4010 | Client Service | ✅ Yes |
| 4015 | Vendor Service | ✅ Yes |
| 4020 | Trip Service | ✅ Yes |
| 4025 | Billing Service | ✅ Yes |
| 4035 | Employee Service | ✅ Yes |
| 3307 | MySQL | ⚠️ Optional |

## ⚡ What Gets Deployed

### Backend (Docker Containers)
- MySQL 8.0 database
- Auth Service (Port 4005)
- Client Service (Port 4010)
- Vendor Service (Port 4015)
- Trip Service (Port 4020)
- Billing Service (Port 4025)
- Employee Service (Port 4035)

### Frontend (PM2)
- React + Vite application (Port 5173)
- Auto-configured with Azure IP
- Auto-restart on system reboot

## 🎯 Production Checklist

Before going live:
- [ ] Change default MySQL password
- [ ] Change default user passwords
- [ ] Set up HTTPS/SSL certificate
- [ ] Configure proper CORS origins
- [ ] Enable Azure monitoring
- [ ] Set up backup strategy
- [ ] Configure auto-scaling (if needed)

## 💡 Tips

1. **Save your Azure IP**: You'll need it to access the application
2. **Wait patiently**: Initial deployment takes 3-5 minutes
3. **Check logs**: If something fails, check Docker and PM2 logs
4. **Restart services**: Most issues can be fixed with a restart

## 🆘 Common Issues

**Services not starting?**
```bash
cd ~/moveinsync/backend
docker-compose -f docker-compose.prod.yml logs -f
```

**Cannot access from browser?**
- Verify Network Security Group has all ports open
- Check UFW firewall: `sudo ufw status`

**Frontend not loading?**
```bash
pm2 logs moveinsync-frontend
pm2 restart moveinsync-frontend
```

## 📞 Support

For issues:
1. Check AZURE_DEPLOYMENT_GUIDE.md troubleshooting section
2. View service logs
3. Verify all containers/processes are running

---

**Ready to deploy?** Start with [AZURE_DEPLOYMENT_GUIDE.md](./AZURE_DEPLOYMENT_GUIDE.md)! 🚀

