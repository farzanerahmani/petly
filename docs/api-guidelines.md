# Petly API Guidelines

## 1\. Base URL

All API endpoints use the following base path:

```text
/api/v1
```

Example:

```text
GET /api/v1/health
```

## 2\. API Versioning

The current API version is:

```text
v1
```

All public endpoints should use:

```text
/api/v1/
```

Future API versions may use:

```text
/api/v2/
```

## 3\. HTTP Methods

### GET

Used to retrieve resources.

Examples:

```text
GET /api/v1/pets
GET /api/v1/pets/{id}
```

### POST

Used to create a new resource.

Example:

```text
POST /api/v1/pets
```

Successful creation returns:

```text
201 Created
```

### PUT

Used to replace an existing resource.

Example:

```text
PUT /api/v1/pets/{id}
```

Successful update returns:

```text
200 OK
```

### PATCH

Used for partial updates.

Example:

```text
PATCH /api/v1/pets/{id}
```

Successful update returns:

```text
200 OK
```

### DELETE

Used to remove a resource.

Example:

```text
DELETE /api/v1/pets/{id}
```

Successful deletion returns:

```text
204 No Content
```

## 4\. HTTP Status Codes

|Status|Meaning|
|-|-|
|200|Request succeeded|
|201|Resource created|
|204|Request succeeded with no response body|
|400|Invalid request|
|404|Resource not found|
|409|Resource conflict|
|500|Unexpected server error|

## 5\. Success Response

Successful API responses use:

```text
ApiResponse<T>
```

Example:

```json
{
  "data": {
    "id": 1,
    "name": "Milo"
  }
}
```

For collections:

```json
{
  "data": \\\\\\\[
    {
      "id": 1,
      "name": "Milo"
    },
    {
      "id": 2,
      "name": "Luna"
    }
  ]
}
```

## 6\. Error Response

Error responses use:

```text
ApiError
```

Example:

```json
{
  "code": "PET\\\\\\\_NOT\\\\\\\_FOUND",
  "message": "Pet not found",
  "details": {}
}
```

The `code` should be stable and machine-readable.

The `message` should be human-readable.

The `details` object may contain additional error information.

## 7\. Validation Errors

Invalid request data should return:

```text
400 Bad Request
```

Example:

```json
{
  "code": "VALIDATION\\\\\\\_ERROR",
  "message": "Request validation failed",
  "details": {
    "name": "name is required",
    "species": "species is required"
  }
}
```

Validation should be performed at the API boundary using Jakarta Bean Validation.

## 8\. Not Found Errors

When a requested resource does not exist:

```text
404 Not Found
```

Example:

```json
{
  "code": "PET\\\\\\\_NOT\\\\\\\_FOUND",
  "message": "Pet not found",
  "details": {}
}
```

## 9\. Conflict Errors

When an operation conflicts with the current state of a resource:

```text
409 Conflict
```

Example:

```json
{
  "code": "PET\\\\\\\_ALREADY\\\\\\\_EXISTS",
  "message": "Pet already exists",
  "details": {}
}
```

## 10\. Internal Server Errors

Unexpected server errors should return:

```text
500 Internal Server Error
```

Example:

```json
{
  "code": "INTERNAL\\\\\\\_SERVER\\\\\\\_ERROR",
  "message": "An unexpected error occurred",
  "details": {}
}
```

Internal implementation details and stack traces should not be exposed to API clients.

## 11\. JSON

The API uses:

```text
application/json
```

for request and response bodies where a body is required.

Clients should send:

```text
Content-Type: application/json
```

when sending JSON request bodies.

## 12\. Resource Naming

REST resources should use plural nouns.

Preferred:

```text
/pets
/foods
/inventory
/reminders
```

Avoid:

```text
/getPets
/createPet
/deletePet
```

The HTTP method already describes the operation.

## 13\. Resource IDs

Resources should be accessed by ID using:

```text
/{resource}/{id}
```

Example:

```text
GET /api/v1/pets/123
```

## 14\. Controller Rules

Controllers should:

* Handle HTTP concerns.
* Validate incoming requests.
* Call services.
* Return appropriate HTTP status codes.

Controllers should not contain business logic or direct database access.

## 15\. Service Rules

Services should:

* Contain business logic.
* Coordinate application workflows.
* Call repositories when persistence is required.
* Remain independent of HTTP-specific concerns where possible.

## 16\. Repository Rules

Repositories should:

* Handle database access.
* Execute persistence queries.
* Avoid business logic.

## 17\. Error Code Naming

Error codes should use uppercase `SNAKE\\\\\\\_CASE`.

Examples:

```text
VALIDATION\\\\\\\_ERROR
PET\\\\\\\_NOT\\\\\\\_FOUND
PET\\\\\\\_ALREADY\\\\\\\_EXISTS
INTERNAL\\\\\\\_SERVER\\\\\\\_ERROR
```

Error codes should remain stable because mobile clients may use them for specific UI behavior.

## 18\. API Design Principles

The API should remain:

* Consistent
* Predictable
* Simple
* Mobile-friendly
* Backward-compatible within the same API version

API design decisions should favor clarity and consistency over unnecessary complexity.

