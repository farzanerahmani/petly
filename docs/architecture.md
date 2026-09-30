# Petly Backend Architecture

## 1. Overview

Petly backend is a modular monolith built with Spring Boot.

The backend exposes REST APIs consumed by the Flutter mobile application.

The architecture is designed to keep the MVP simple while maintaining clear boundaries between business features.

## 2. Technology Stack

- Java 21
- Spring Boot
- Maven
- PostgreSQL
- REST / JSON
- Flutter mobile client

## 3. Package Structure

```text
com.petly
├── BackendApplication.java
├── common/
│   ├── controller/
│   │   └── HealthController.java
│   ├── exception/
│   │   ├── ApiError.java
│   │   └── GlobalExceptionHandler.java
│   └── response/
│       └── ApiResponse.java
└── pet/
    ├── controller/
    ├── service/
    │   └── PetService.java
    ├── repository/
    └── entity/
```

## 4. Root Package

The root package is:

```text
com.petly
```

`BackendApplication` is located directly inside the root package.

Spring Boot component scanning therefore discovers application components under:

```text
com.petly.*
```

## 5. Common Package

The `common` package contains components shared across multiple features.

### common.controller

Contains shared or system-level controllers.

Example:

```text
HealthController
```

### common.exception

Contains application-wide exception handling.

Main components:

- `ApiError`
- `GlobalExceptionHandler`

### common.response

Contains common API response structures.

Main component:

- `ApiResponse`

## 6. Feature Packages

Business features are organized into their own packages.

Example:

```text
pet/
├── controller/
├── service/
├── repository/
└── entity/
```

Future features can follow the same structure:

```text
food/
inventory/
reminder/
growth/
assistant/
marketplace/
```

## 7. Layer Responsibilities

### Controller

Responsible for:

- Receiving HTTP requests
- Request validation
- Calling application services
- Returning HTTP responses

Controllers should not contain business logic.

### Service

Responsible for:

- Business logic
- Use cases
- Application workflows
- Coordinating repositories and other services

### Repository

Responsible for:

- Database access
- Persistence operations
- Database queries

Repositories should not contain business rules.

### Entity

Represents the persistence model used by the database.

Entities primarily represent stored data and relationships.

## 8. Dependency Direction

The primary dependency flow is:

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
Database
```

Controllers should not directly access repositories.

Business logic should remain inside the service layer.

## 9. API Response Structure

Successful API responses use `ApiResponse<T>`.

Example:

```json
{
  "data": {}
}
```

## 10. API Error Structure

API errors use `ApiError`.

Example:

```json
{
  "code": "VALIDATION_ERROR",
  "message": "Request validation failed",
  "details": {
    "name": "name is required"
  }
}
```

## 11. Global Exception Handling

Application exceptions are handled centrally by:

```text
GlobalExceptionHandler
```

The handler is responsible for converting application exceptions into consistent API error responses.

The current implementation supports:

- Validation errors
- Not found errors
- Unexpected server errors

## 12. API Versioning

All public APIs currently use version `v1`.

Base path:

```text
/api/v1
```

Example:

```text
GET /api/v1/health
```

## 13. Architecture Style

Petly uses a modular monolith architecture.

All features initially run inside a single Spring Boot application.

Features are separated by business domain while remaining inside the same application.

This keeps MVP development simple while allowing the system to evolve as the product grows.

## 14. Development Principles

The backend should follow these principles:

- Keep controllers thin.
- Keep business logic inside services.
- Keep database access inside repositories.
- Keep shared components inside `common`.
- Organize business functionality by feature.
- Use consistent API responses.
- Use consistent error responses.
- Validate incoming requests.
- Avoid premature abstractions.
- Prefer simple solutions during MVP development.
- Keep the API consistent, predictable, and simple.
- Favor clarity and maintainability over unnecessary complexity.

Petly — Phase 1 ERD
Overview

Phase 1 focuses on the Pet Profile domain and the minimum structured data required for future Growth Tracking, Calorie Calculation, Food Recommendation, and ML.

ERD
users
  │
  │ 1:N
  ▼
pets
  │
  ├──── 1:N ──── pet_weight_records
  │
  └──── 1:N ──── pet_body_condition_records
1. users
Column	Type	Constraints
id	BIGINT	PK
phone_number	VARCHAR(20)	UNIQUE, NOT NULL
created_at	TIMESTAMPTZ	NOT NULL
updated_at	TIMESTAMPTZ	NOT NULL
Notes
Phone number is the User's login identifier.
Phone number is stored in normalized international format.
Authentication will use OTP in a future authentication feature.
OTP data is not stored in the users table.
2. pets
Column	Type	Constraints
id	BIGINT	PK
owner_id	BIGINT	NOT NULL, FK → users.id
name	VARCHAR(100)	NOT NULL
species	VARCHAR(30)	NOT NULL
breed	VARCHAR(100)	NULL
gender	VARCHAR(20)	NOT NULL
birth_date	DATE	NULL
activity_level	VARCHAR(30)	NOT NULL
created_at	TIMESTAMPTZ	NOT NULL
updated_at	TIMESTAMPTZ	NOT NULL
Business Rules
Each Pet belongs to exactly one User.
A User can own multiple Pets.
name must not be blank.
birth_date, when provided, cannot be in the future.
species is a controlled value.
gender is a controlled value.
activity_level is a controlled value.
Pet ownership must be checked on every Pet operation.
Enums
PetSpecies
- DOG
- CAT
- BIRD
- RABBIT
- OTHER
PetGender
- MALE
- FEMALE
- UNKNOWN
ActivityLevel
- LOW
- MODERATE
- HIGH
- VERY_HIGH
3. pet_weight_records
Column	Type	Constraints
id	BIGINT	PK
pet_id	BIGINT	NOT NULL, FK → pets.id
weight	DECIMAL(...)	NOT NULL
recorded_at	TIMESTAMPTZ	NOT NULL
Business Rules
Weight is historical data.
A new measurement creates a new record.
Previous weight records are not overwritten during normal updates.
This history will support future Growth Tracking and ML.
4. pet_body_condition_records
Column	Type	Constraints
id	BIGINT	PK
pet_id	BIGINT	NOT NULL, FK → pets.id
score	INTEGER	NOT NULL
source	VARCHAR(30)	NOT NULL
assessed_at	TIMESTAMPTZ	NOT NULL
Business Rules
Body condition is an assessment/observation, not a permanent Pet attribute.
Assessments are historical.
source identifies how the assessment was produced.
Future sources may include owner assessment or AI-based assessment.
Relationships
User
  │
  │ 1
  │
  │ owns
  │
  │ N
  ▼
Pet
  │
  ├── 1:N ──> PetWeightRecord
  │
  └── 1:N ──> PetBodyConditionRecord
Intentionally Out of Scope for Phase 1

The following are not implemented as Phase 1 tables:

Allergies
Diseases / Health Conditions
Dietary Restrictions
Photos
Daily Calorie Calculations
Food Recommendations
OTP
Authentication Tokens

These will be introduced as separate domains/features when their requirements are implemented.

Design Principles
Keep Pet focused on stable profile data.
Store time-varying observations as historical records.
Do not store derived values such as age as persistent Pet fields.
Do not store recommendation outputs directly on Pet.
Keep analytical data structured rather than relying on free-text fields.
Avoid premature over-engineering while preserving future extensibility for Growth Tracking, Recommendation, and ML.
