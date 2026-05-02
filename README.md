# Ecommerce Modular Monolith

This repository contains the Week 1 foundation for a solo-built ecommerce platform inspired by Amazon's core buying flow. The application is intentionally starting as a modular monolith so we can ship quickly, keep operational overhead low, and still preserve clean seams between domains.

## Week 1 outcomes

- Spring Boot 3 + Java 21 project scaffolded with Maven
- Modular package boundaries defined with Spring Modulith
- MySQL + Flyway configuration added
- Local, dev, and test environment profiles added
- Initial schema migration created
- Product and architecture docs written

## Week 2 outcomes

- GitHub Actions CI workflow added
- Dev deployment workflow skeleton added
- Local MySQL bootstrapping with Docker Compose added
- Request correlation and global API error handling added
- EC2 deployment runbook and environment template added

## Tech stack

- Java 21
- Spring Boot
- Spring Modulith
- MySQL
- Flyway
- AWS-ready configuration conventions
- GitHub-friendly repository structure

## Module map

- `identity`
- `customer`
- `catalog`
- `pricing`
- `inventory`
- `cart`
- `checkout`
- `ordering`
- `payment`
- `shipping`
- `notification`
- `admin`
- `shared`

## Local development

1. Copy the environment template if you want a local file-driven setup:

```powershell
Copy-Item .env.example .env
```

2. Start MySQL locally:

```powershell
docker compose up -d mysql
```

3. Set environment variables:

```powershell
$env:DB_USERNAME="root"
$env:DB_PASSWORD="your-password"
```

4. Run the application:

```powershell
mvn spring-boot:run
```

The default profile is `local`. For `dev`, run:

```powershell
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

## Test

```powershell
mvn test
```

## Documentation

- [Vision](docs/vision.md)
- [MVP Scope](docs/mvp-scope.md)
- [Non-Functional Requirements](docs/nfr.md)
- [Architecture](docs/architecture.md)
- [Modules](docs/modules.md)
- [Backlog](docs/backlog.md)
- [Dev Deployment Runbook](docs/runbooks/dev-deployment.md)

## CI/CD

- `.github/workflows/ci.yml` runs build/test on pushes and PRs
- `.github/workflows/deploy-dev.yml` is a manual dev deployment skeleton for EC2-based delivery

