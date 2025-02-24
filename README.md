<strong style="font-size: 2.5em;">ecommerce - Goals & Objectives</strong>

This project aim to develop a backend service for an ecommerce platform similar to Amazon, with an emphasis on implementing the Domain-Driven Design (DDD) approach and eventually transitioning to a Microservices architecture.


<details>
    <summary><strong style="font-size: 2.5em;">ecommerce - Current Progress</strong></summary>

[My detailed design process in Notion](https://sphenoid-soybean-e9a.notion.site/E-Commerce-Project-15bce826ff1180ebae6ef3ef200f857b)

## **User Context**

- Designing DTOs for User entity
- Refactoring controllers, services to use UserDTOs
- Create domain and app services for Role, Permission
- Enforce RBAC authorization on endpoints

## **Product Context**
- Develop the business logic for adding, removing, updating products
- Only sellers can add/update/remove products
</details>

<details>
    <summary><strong style="font-size: 2.5em;">ecommerce - Bounded Contexts</strong></summary>

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
</details>
