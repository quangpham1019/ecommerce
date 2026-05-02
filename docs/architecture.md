# Architecture

## Style

The system is a modular monolith. It is deployed as one application but internally organized into business modules with clear boundaries. This keeps delivery fast for a solo developer while avoiding the tight coupling of a traditional layered monolith.

## Architectural principles

- One deployable unit for MVP
- Business modules own their domain logic
- Cross-module calls happen through explicit APIs or events
- Avoid direct database coupling between unrelated modules
- Keep infrastructure details behind module boundaries

## Core stack

- Spring Boot for application runtime
- Spring Modulith for module boundary clarity
- MySQL for primary relational storage
- Flyway for schema migrations
- AWS for infrastructure
- GitHub for source control and CI/CD

## Deployment model

MVP deployment target:

- Spring Boot application on EC2
- MySQL on RDS
- Product assets on S3
- Logs and alarms in CloudWatch

## Initial technical decisions

- Use package-based modules instead of separate deployables
- Use synchronous calls inside the monolith first
- Add domain events only where they help decouple workflow transitions
- Keep persistence simple with Spring Data JPA at the start

