# Non-Functional Requirements

## Availability

- MVP target availability: 99.5%
- Single-region deployment is acceptable for MVP

## Performance

- Catalog listing p95 under 500 ms for normal load
- Product detail p95 under 400 ms
- Checkout request p95 under 1000 ms excluding third-party payment latency

## Security

- Authenticated endpoints protected with Spring Security
- Secrets stored outside source control
- Audit logs for admin-sensitive actions
- Input validation on all external request payloads

## Maintainability

- Strong module boundaries
- Flyway-based schema evolution
- Consistent coding standards and test conventions

## Operability

- Structured logs
- Actuator health checks
- CloudWatch-ready log strategy
- Database backup strategy before production launch

## Cost

- Prefer cost-effective AWS primitives during MVP
- Start with EC2, RDS, S3, and CloudWatch

