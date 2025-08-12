# Prueba Ingreso – API Documentation

Base URL: `http://localhost:8080/api/v1`

This document describes the public REST endpoints of the project, inferred from the source code in `controllers/FrontController.java` and `controllers/AdminController.java`.

---

## Authentication
_No authentication_ is implemented. All endpoints are public. For production, consider adding auth and limiting `/api/v1/admin/*` to a restricted profile.

## Content Type
All requests and responses use `application/json` unless noted.

## Error Format
Validation errors return **400 Bad Request** with body:

```json
{
  "status": 400,
  "error": "Bad Request",
  "errors": [
    { "field": "usuarioNombre", "message": "size must be between 5 and 50" }
  ]
}
```

---

## Endpoints

### Create Item
**POST** `/item`  
Request body (`ReqAddItemDto`):
```json
{'name': 'Televisor Samsung 55"'}
```
Response (`ResItemDto`):
```json
{'id': 3, 'name': 'iPhone 15', 'idApuestas': [10, 11, 15]}
```

### Get Item by ID
**GET** `/item/{id}`  
Response (`ResItemDto`).

### List Items
**GET** `/item`  
Response (`ResGetItemsDto`):
```json
{'items': [{'id': 1, 'name': 'TV', 'idApuestas': [1]}, {'id': 2, 'name': 'Laptop', 'idApuestas': []}]}
```

### Create Bet (Apuesta)
**POST** `/apuesta`  
Validations: `usuarioNombre` length 5–50; `montoApuesta` 1000–999,999,999; `itemId` required.  
Request body (`ReqAddApuestaDto`):
```json
{'itemId': 1, 'usuarioNombre': 'Carlos Perez', 'montoApuesta': 15000}
```
Response (`ResApuestaDto`):
```json
{'id': 42, 'amount': 15000}
```

### Get Winner for Item
**GET** `/winner/{itemId}`  
Response (`ResWinnerDto`) or `null` if item has no bets:
```json
{'itemId': 1, 'itemName': 'TV', 'usuarioId': 5, 'usuarioNombre': 'Carolina', 'montoApuesta': 99000}
```

### Get User Total Bet
**GET** `/users/{userId}/bets/total`  
Response (`ResTotalBetUserDto`):
```json
{'userId': 7, 'total': 35000}
```

### List Bets for a User
**GET** `/users/{userId}/apuestas`  
Response: array of `ResApuestaDetailDto`:
```json
[{'id': 12, 'itemId': 3, 'itemName': 'iPhone 15', 'amount': 25000}]
```

### Get User
**GET** `/users/{userId}`  
Response (`ResUsuarioDto`):
```json
{'id': 7, 'nombre': 'Bob'}
```

### (Admin) List All Bets
**GET** `/admin/apuestas`  
Response: array of admin bet views:
```json
[{'id': 1, 'amount': 2000, 'itemId': 10, 'userName': 'Alice'}]
```

---

## cURL Examples

```bash
# Create an item
curl -X POST http://localhost:8080/api/v1/item   -H "Content-Type: application/json"   -d '{"name":"TV"}'

# Place a bet
curl -X POST http://localhost:8080/api/v1/apuesta   -H "Content-Type: application/json"   -d '{"itemId":1,"usuarioNombre":"Carlos Perez","montoApuesta":15000}'

# Get current winner for item 1
curl http://localhost:8080/api/v1/winner/1

# Get user's total bet
curl http://localhost:8080/api/v1/users/7/bets/total

# List bets for user
curl http://localhost:8080/api/v1/users/7/apuestas

# Admin: list all bets
curl http://localhost:8080/api/v1/admin/apuestas
```

---

## Notes & Assumptions
- Error format is based on the global `RestExceptionHandler` for validation errors.
- There is no authentication/authorization layer in the code as provided.
- The admin endpoints are meant for dev/test; consider enabling Spring `@Profile` guards in production.
- See `compose.yaml` / `docker-compose.yml` for local Postgres setup and ports.
- Build tool: Gradle. Run app with `./gradlew bootRun` (or via IntelliJ).

