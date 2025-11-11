# 🔒 Security Setup Guide

## Important: Protecting Credentials

Your application contains sensitive information that should **NEVER** be committed to Git:
- Database passwords
- JWT secret keys
- API keys
- Private configuration

## What We've Done

1. ✅ Updated `.gitignore` to exclude:
   - `.env` files
   - `application-dev.properties`
   - `application-prod.properties`

2. ✅ Created `.env.example` as a template

3. ✅ Updated README to show environment variable usage

## Steps to Secure Your Application

### 1. Remove Hardcoded Credentials

Check these files and replace hardcoded passwords:

```bash
backend/auth-service/src/main/resources/application.properties
backend/client-service/src/main/resources/application.properties
backend/vendor-service/src/main/resources/application.properties
backend/trip-service/src/main/resources/application.properties
backend/billing-service/src/main/resources/application.properties
backend/employee-service/src/main/resources/application.properties
backend/docker-compose.yml
```

Replace:
```properties
spring.datasource.password=Qwerty@cs12345
```

With:
```properties
spring.datasource.password=${DB_PASSWORD}
```

### 2. Create Local .env File

```bash
# Copy the example file
cp .env.example .env

# Edit with your actual credentials
nano .env
```

### 3. Update Docker Compose

In `backend/docker-compose.yml`, replace:
```yaml
environment:
  MYSQL_ROOT_PASSWORD: Qwerty@cs12345
  SPRING_DATASOURCE_PASSWORD: Qwerty@cs12345
```

With:
```yaml
environment:
  MYSQL_ROOT_PASSWORD: ${MYSQL_ROOT_PASSWORD}
  SPRING_DATASOURCE_PASSWORD: ${DB_PASSWORD}
```

### 4. Load Environment Variables

**On Windows (PowerShell):**
```powershell
$env:DB_USERNAME="root"
$env:DB_PASSWORD="your_password"
$env:JWT_SECRET="your_secret"
```

**On Linux/Mac:**
```bash
export DB_USERNAME=root
export DB_PASSWORD=your_password
export JWT_SECRET=your_secret
```

**With Docker Compose:**
```bash
# Docker automatically loads .env file in the same directory
cd backend
docker-compose up -d
```

### 5. Verify .env is Ignored

```bash
git status
# .env should NOT appear in the list
```

## Before Pushing to GitHub

Run this checklist:

- [ ] No passwords in `application.properties` files
- [ ] No passwords in `docker-compose.yml`
- [ ] No passwords in README.md
- [ ] `.env` file is in `.gitignore`
- [ ] Only `.env.example` is committed (with placeholder values)
- [ ] Verified with `git status` - no sensitive files listed

## If You Already Committed Credentials

If you've already committed passwords to Git, you need to:

1. **Change the passwords immediately** (they're now public!)
2. Remove them from Git history:

```bash
# This rewrites Git history - use carefully!
git filter-branch --force --index-filter \
  "git rm --cached --ignore-unmatch backend/*/src/main/resources/application.properties" \
  --prune-empty --tag-name-filter cat -- --all

# Force push (WARNING: This affects all collaborators)
git push origin --force --all
```

3. Better option: Create a new repository and start fresh

## Production Deployment

For production, use:
- Azure Key Vault
- AWS Secrets Manager
- HashiCorp Vault
- Kubernetes Secrets

Never use plain text credentials in production!

---

**Remember:** Security is not optional. Take these steps before sharing your code! 🔒

