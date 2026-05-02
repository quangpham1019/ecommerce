# Modules

## Identity

Owns authentication, authorization, user credentials, and role assignment.

## Customer

Owns customer profiles, account preferences, and saved addresses.

## Catalog

Owns products, categories, descriptions, and browse/search metadata.

## Pricing

Owns pricing rules, base price snapshots, and future promotion calculation seams.

## Inventory

Owns stock counts, availability, and stock adjustment workflows.

## Cart

Owns cart state and cart item mutation logic.

## Checkout

Owns checkout orchestration, validation, and order submission readiness.

## Ordering

Owns orders, order line items, and order state transitions.

## Payment

Owns payment provider integration and payment transaction state.

## Shipping

Owns shipment lifecycle and fulfillment-related data.

## Notification

Owns outbound email and event-driven customer/admin notifications.

## Admin

Owns internal operational use cases that compose other modules behind privileged interfaces.

## Shared

Contains shared technical abstractions and cross-cutting support code only. It should not become a dumping ground for business logic.

