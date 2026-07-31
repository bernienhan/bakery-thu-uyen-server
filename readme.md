# Bakery Shop Backend

A Java 21 / Spring Boot backend for a bakery shop, built as a modular monolith with lightweight Clean Architecture boundaries.

The project currently focuses on identity, users, and stateful authentication backed by PostgreSQL, Flyway, and Redis.

## Tech Stack

- Java 21
- Spring Boot 4
- Spring Web
- Spring Security
- Spring Data JPA
- PostgreSQL
- Flyway
- Redis
- Actuator
- Springdoc OpenAPI
- Lombok

## Architecture

Base package:

```text
com.thuyen.bakeryshop
├─ common
│  ├─ config
│  ├─ constant
│  ├─ exception
│  ├─ response
│  ├─ security
│  └─ util
└─ modules
   ├─ auth
   └─ user
```

Each module follows this shape:

```text
module
├─ api              # controllers, request/response DTOs, API mappers
├─ application      # use cases, application DTOs, orchestration
├─ domain           # domain models, enums, repository interfaces
└─ infrastructure   # JPA, Redis, external details, repository implementations
```

Dependency direction:

```text
api -> application -> domain
infrastructure -> domain/application
```

Domain code should not depend on Spring MVC, JPA entities, Redis, or HTTP request classes.

## Auth Overview

Authentication is designed as stateful session-based auth:

- `users` and `roles` are owned by the user module.
- `user_credentials`, `auth_sessions`, tokens, providers, and auth records are owned by the auth module.
- PostgreSQL is the source of truth.
- Redis caches active auth sessions for faster lookup.

Current core endpoints:

```text
POST /api/v1/auth/register
POST /api/v1/auth/login
```

Register creates:

```text
users
user_credentials
```

Login:

```text
find user by email/phone
verify password hash
create auth_sessions row
cache session in Redis
write security event
return session info and user data
```

## Response Format

All API responses use a common wrapper:

```json
{
  "success": true,
  "code": "AUTH_SUCCESS_002",
  "message": "Login successfully",
  "data": {}
}
```

Errors use stable module-prefixed codes:

```text
COMMON_001
AUTH_001
USER_001
PRODUCT_001
```

Success codes follow the same idea:

```text
COMMON_SUCCESS_001
AUTH_SUCCESS_001
USER_SUCCESS_001
PRODUCT_SUCCESS_001
```

## Local Setup

Create an application config from the example:

```powershell
Copy-Item src/main/resources/application-example.properties src/main/resources/application.properties
```

PostgreSQL example:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/bakery_shop
spring.datasource.username=postgres
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=none
```

Redis properties:

```properties
spring.data.redis.host=127.0.0.1
spring.data.redis.port=6379
spring.data.redis.password=redis123456
spring.data.redis.timeout=3s
```

Start Redis:

```powershell
$env:REDIS_PASSWORD="redis123456"; docker compose up -d redis
```

Test Redis:

```powershell
docker exec -it redis-local redis-cli -a redis123456 ping
```

Expected:

```text
PONG
```

## Database

Flyway migrations live in:

```text
src/main/resources/db/migration
```

Current migrations:

```text
V1__create_identity_auth_security_tables.sql
V2__update_security_column_types.sql
V3__add_auth_sessions_and_update_refresh_tokens.sql
V4__seed_default_roles.sql
```

Default roles:

```text
ADMIN
USER
```

For local development, if a migration was already applied and then edited, reset the local database or clean up `flyway_schema_history` intentionally before rerunning.

## Run

Compile:

```powershell
.\gradlew compileJava
```

Run tests:

```powershell
.\gradlew test
```

Start the app:

```powershell
.\gradlew bootRun
```

Swagger UI:

```text
http://localhost:8080/swagger-ui/index.html
```

Health check:

```text
http://localhost:8080/actuator/health
```

## Development Notes

- Keep mapping logic in dedicated mappers.
- Do not map JPA entities directly in controllers or services.
- Application services coordinate use cases and transactions.
- Repository interfaces stay in domain.
- JPA repositories and repository implementations stay in infrastructure.
- Redis session code belongs to auth infrastructure, while shared Redis keys live in common constants.
