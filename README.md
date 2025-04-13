<strong style="font-size: 2.5em;">ecommerce - Goals & Objectives</strong>

This project aim to develop a backend service for an ecommerce platform similar to Amazon, with an emphasis on implementing the Domain-Driven Design (DDD) approach and eventually transitioning to a Microservices architecture.


<details>
    <summary><strong style="font-size: 2.5em;">Run app with Docker</strong></summary>

## **Setup**
- Install Docker.
- Clone the repository.
- Open terminal at the project root.
- Run command "docker build -t ecommerce ." to:
  - Build JAR file within docker container, and 
  - Create a docker image of Ecommerce app.
- Run command "docker compose up", which do the followings:
  - Create a container for MySQL database at port 3307:3306 (to prevent potential conflict with any local db server).
  - Create a container for Ecommerce app with corresponding database environment variables.
- The app will be initialized at http://localhost:5000
- Swagger API docs is available at http://localhost:5000/api-docs

## **Login and access API Docs**
- Access login page at http://localhost:5000/login
  - Username: admin@gmail.com
  - Password: qweQWE123!
  - You will be logged in as ADMIN
- The app will return a JSON with two properties
  - Copy the JWT token to authorize with Product context
- Go to Swagger API docs at http://localhost:5000/api-docs
  - You are authenticated with User context through Spring Session
  - To authenticate with Product context
    - Click on the "Authorize" with Unlock icon
    - Enter the JWT token and click "Authorize"

</details>
<details>
    <summary><strong style="font-size: 2.5em;">ecommerce - Current Progress</strong></summary>

- Currently, only the backend is set up with Swagger API docs.
- Frontend will be integrated later.

- [My detailed design process in Notion](https://sphenoid-soybean-e9a.notion.site/E-Commerce-Project-15bce826ff1180ebae6ef3ef200f857b)

## **User Context**

- Increase test coverage.

## **Product Context**
- Develop the business logic for adding, removing, updating products.
- Only sellers can add/update/remove products.

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