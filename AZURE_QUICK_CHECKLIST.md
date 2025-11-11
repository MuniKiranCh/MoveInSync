# Quick Azure Deployment Checklist

## ✅ Pre-Deployment

- [ ] Azure account created
- [ ] Project pushed to GitHub (with all latest files)
- [ ] Removed sensitive data from code

## 🖥️ Azure VM Setup

- [ ] Created VM: Ubuntu 22.04, Standard_B2s (2 vCPU, 4GB RAM)
- [ ] Noted down Public IP: ________________
- [ ] Configured Network Security Group with ports:
  - [ ] 22 (SSH)
  - [ ] 80 (HTTP)
  - [ ] 443 (HTTPS)
  - [ ] 5173 (Frontend)
  - [ ] 4005, 4010, 4015, 4020, 4025, 4035 (Backend Services)
  - [ ] 3307 (MySQL - optional)

## 📂 Upload Files

- [ ] Connected via SSH: `ssh azureuser@YOUR_IP`
- [ ] Cloned repository: `git clone https://github.com/YOUR_USERNAME/MoveInSync.git`
- [ ] Renamed folder: `mv MoveInSync moveinsync`

## 🔧 Setup

- [ ] Made scripts executable: `chmod +x azure-setup.sh deploy-azure.sh`
- [ ] Ran setup: `./azure-setup.sh`
- [ ] Logged out and back in: `exit` then reconnect

## 🚀 Deployment

- [ ] Ran deployment: `./deploy-azure.sh`
- [ ] Waited 3-5 minutes for services to start
- [ ] Verified backend: `docker ps` (should show 7 containers)
- [ ] Verified frontend: `pm2 status` (should show running)

## 🌐 Access

- [ ] Frontend works: `http://YOUR_IP:5173`
- [ ] Login successful: `admin@moveinsync.com / password`
- [ ] Can view dashboard
- [ ] Backend APIs responding

## 📝 Post-Deployment

- [ ] Bookmark: `http://YOUR_IP:5173`
- [ ] Share IP with team
- [ ] Set up monitoring (optional)
- [ ] Schedule regular backups (optional)

---

## 🆘 Quick Troubleshooting

**Services not starting?**
```bash
cd ~/moveinsync/backend
docker-compose -f docker-compose.prod.yml logs -f
```

**Frontend not loading?**
```bash
pm2 logs moveinsync-frontend
pm2 restart moveinsync-frontend
```

**Cannot access from browser?**
- Check Azure Portal → VM → Networking → Inbound port rules
- Verify all ports are added

**Need to restart everything?**
```bash
cd ~/moveinsync/backend
docker-compose -f docker-compose.prod.yml restart
pm2 restart moveinsync-frontend
```

---

## 📞 Important Commands

```bash
# View all services
docker ps
pm2 status

# View logs
docker-compose -f docker-compose.prod.yml logs -f
pm2 logs

# Restart
docker-compose -f docker-compose.prod.yml restart
pm2 restart moveinsync-frontend

# Update code
cd ~/moveinsync
git pull
./deploy-azure.sh
```

---

Your application URL: `http://YOUR_AZURE_IP:5173`

