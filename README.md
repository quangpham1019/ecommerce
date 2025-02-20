# ecommerce - Goals & Objectives

This project aim to develop a backend service for an ecommerce platform similar to Amazon, with an emphasis on implementing the Domain-Driven Design (DDD) approach and eventually transitioning to a Microservices architecture.

# ecommerce - Bounded Contexts

This project is designed around the Domain-Driven Design (DDD) approach, with separate bounded contexts to encapsulate distinct areas of the e-commerce platform's domain. Each context has its own responsibilities and domain logic.

## **User Context**
- Manages user CRUD operations
- Handles user authorization based on roles and permissions
- Manages role and permission CRUD operations

## **Product Context**
- Manages product CRUD operations

## **Cart Context**
- Manages cart CRUD operations
- Handles cart persistence logic (ephemeral carts, save-for-later functionality)

## **Order Context**
- Manages order CRUD operations for record-keeping and status tracking

## **Payment Context**
- Manages payment processing via external third-party integrations
- Handles price breakdowns for orders
- Manages user subscriptions and related payment plans
