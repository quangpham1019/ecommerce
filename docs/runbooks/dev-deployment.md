# Dev Deployment Runbook

## Goal

Provide a repeatable deployment path for the dev environment using GitHub Actions and a single EC2 host.

## AWS services

- EC2 for application hosting
- RDS MySQL for database
- S3 for future asset storage
- CloudWatch for logs and metrics
- Systems Manager Parameter Store or Secrets Manager for secrets

## EC2 baseline

- Ubuntu or Amazon Linux
- Java 21 installed
- `ecommerce-dev` systemd service configured
- Application home at `/opt/ecommerce/current`
- Release directory at `/opt/ecommerce/releases`

## Required GitHub secrets

- `DEV_EC2_HOST`
- `DEV_EC2_USERNAME`
- `DEV_EC2_SSH_KEY`

## Required application environment variables on EC2

- `SPRING_PROFILES_ACTIVE=dev`
- `DB_HOST`
- `DB_PORT`
- `DB_NAME`
- `DB_USERNAME`
- `DB_PASSWORD`

## Deployment flow

1. Trigger the `Deploy Dev` GitHub Actions workflow.
2. Choose the branch or ref to deploy.
3. Workflow builds the jar.
4. Workflow copies the jar to EC2.
5. Workflow restarts `ecommerce-dev`.
6. Verify `/actuator/health` after restart.

## Verification checklist

- App process is running
- Health endpoint returns `UP`
- Database connectivity works
- CloudWatch or local journal logs show clean startup

## Rollback

1. Identify the last known good jar in `/opt/ecommerce/releases`.
2. Copy it back to `/opt/ecommerce/current/ecommerce.jar`.
3. Restart `ecommerce-dev`.
4. Re-check health endpoint and logs.

