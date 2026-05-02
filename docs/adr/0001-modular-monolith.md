# ADR 0001: Modular Monolith For MVP

## Status

Accepted

## Context

The project is being built by a solo developer and needs to reach a working MVP quickly without the operational burden of distributed systems.

## Decision

Adopt a modular monolith architecture using Spring Boot and package-based module boundaries reinforced by Spring Modulith.

## Consequences

- Faster initial delivery and simpler deployment
- Lower AWS and operational complexity
- Cleaner path to future extraction than a tightly coupled monolith
- Requires discipline to maintain module boundaries over time

