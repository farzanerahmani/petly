# Petly Development Standards

## 1. General Principles

- Keep the code simple and readable.
- Prefer small, focused classes and functions.
- Avoid premature abstraction.
- Keep business logic out of controllers and UI components.
- Do not duplicate business rules across backend and mobile.
- Use explicit names instead of abbreviations.
- Do not commit secrets, passwords, API keys, or local environment files.

## 2. Backend — Java / Spring Boot

### Naming

- Classes: `PascalCase`
- Methods: `camelCase`
- Variables: `camelCase`
- Packages: `lowercase`
- Constants: `UPPER_SNAKE_CASE`

Examples:

```java
PetController
PetService
findPetById()
petId
MAX_PETS
```

### Architecture

- Controllers handle HTTP concerns only.
- Services contain business logic.
- Repositories handle persistence.
- Entities represent database state.
- DTOs represent API input/output when needed.
- Common infrastructure belongs under `com.petly.common`.
- Feature-specific code belongs under its feature package.

Example:

```text
com.petly.pet
├── controller
├── service
├── repository
└── entity
```

### API

- All APIs use `/api/v1`.
- Use RESTful resource naming.
- Use `ApiResponse<T>` for successful responses.
- Use `ApiError` for errors.
- Error codes use `UPPER_SNAKE_CASE`.
- Validate incoming request data.
- Do not expose database entities directly when a DTO is more appropriate.

## 3. Mobile — React Native / Expo / TypeScript

### Naming

- Components: `PascalCase`
- Functions: `camelCase`
- Variables: `camelCase`
- Types: `PascalCase`
- Constants: `UPPER_SNAKE_CASE`

Examples:

```tsx
PetCard
getPetById()
petName
Pet
API_BASE_URL
```

### Architecture

- Screens belong under `src/screens`.
- Reusable UI components belong under `src/components`.
- API communication belongs under `src/services`.
- Shared types belong under `src/types`.
- Configuration belongs under `src/config`.
- Keep screens focused on UI and screen-level state.
- Keep API/network logic outside screens.

## 4. Error Handling

### Backend

- Use centralized exception handling.
- Return meaningful HTTP status codes.
- Do not expose internal exception details to clients.
- Use stable error codes.

### Mobile

- Handle API failures explicitly.
- Do not silently ignore failed requests.
- Show user-friendly error states.
- Do not expose raw technical errors to users.

## 5. Configuration

- Local secrets belong in `.env`.
- `.env` must never be committed.
- `.env.example` contains safe example values.
- Environment-specific configuration should not be hardcoded into business logic.
- API base URLs belong in the configuration layer.

## 6. Git

### Branches

Use descriptive branch names:

```text
feature/pet-profile
fix/pet-validation
chore/update-dependencies
```

### Commits

Use:

```text
feat: add pet profile
fix: handle invalid pet id
refactor: simplify pet service
chore: update dependencies
docs: update development standards
test: add pet service tests
```

### Commit Rules

- Keep commits small and focused.
- One logical change per commit.
- Do not commit broken code intentionally.
- Do not commit generated files unless required.
- Run relevant tests/checks before committing.

## 7. Code Quality

Before completing a task:

- Backend should compile successfully.
- Mobile TypeScript should pass type checking.
- Relevant API endpoints should be tested.
- No temporary debug logs should remain.
- No unused files or temporary test code should remain.
- Changes should be reviewed before commit.

## 8. API Compatibility

- Avoid unnecessary breaking changes.
- Do not rename API fields casually.
- Do not change error codes without a reason.
- API changes should be reflected in mobile types and services.
- Breaking API changes require explicit documentation.

## 9. Dependencies

- Add dependencies only when they solve a real project need.
- Prefer stable and well-maintained packages.
- Avoid adding libraries for functionality that is already simple to implement.
- Keep dependencies updated deliberately rather than automatically.

## 10. Documentation

- Document architectural decisions in `docs/`.
- Keep API conventions documented.
- Update documentation when project conventions change.
- Prefer short, practical documentation over excessive documentation.
