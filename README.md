# Hello-World-Api-SpringBoot

Watch the code explainer video for this project:

[Code Explainer Video](https://drive.google.com/file/d/13X9QLdho2T_k0NlCYZPPUal9lWMNeCtl/view?usp=sharing)

## Requirements

- Java 21+
- Maven 3.9+ (or use the included `./mvnw` wrapper)
- Docker & Docker Compose (optional)

## Running the Application

### Option A — Maven wrapper

```bash
./mvnw spring-boot:run
```

The server starts on `http://localhost:8080`.

### Option B — Docker Compose

```bash
docker compose up --build
```

## Running the Tests

```bash
./mvnw test
```

For the full verify lifecycle (recommended):

```bash
./mvnw verify
```

## API Reference

### `GET /hello-world`

| Parameter | Type   | Required | Description   |
| --------- | ------ | -------- | ------------- |
| `name`    | string | Yes      | Name to greet |

#### Success — 200 OK

First character of `name` is A–M (case-insensitive):

```json
{ "message": "Hello Alice" }
```

#### Error — 400 Bad Request

First character of `name` is N–Z, or `name` is absent/empty:

```json
{ "error": "Invalid Input" }
```

#### Example curl commands

```bash
# Valid name
curl "http://localhost:8080/hello-world?name=alice"
# → {"message":"Hello Alice"}

# Invalid name (N–Z)
curl "http://localhost:8080/hello-world?name=nancy"
# → {"error":"Invalid Input"}

# Missing parameter
curl "http://localhost:8080/hello-world"
# → {"error":"Invalid Input"}
```

## Assumptions

1. **Non-alphabetic first character** (e.g. `123`, `!hi`) → treated as invalid input (400).
   The spec only defines A–M and N–Z; digits and symbols fall outside both ranges.
2. **Leading/trailing whitespace** is trimmed before validation. `" alice"` is treated as `"alice"`.
3. **First letter only** determines validity; the rest of the name is preserved as-is after capitalising the first character.
4. **Case-insensitive** boundary check. `"alice"` and `"ALICE"` both succeed; `"nancy"` and `"NANCY"` both fail.
5. The response capitalises only the first character of the (trimmed) name. `"alice"` → `"Alice"`, `"aLiCe"` → `"ALiCe"`.
6. **Single-character names** (e.g. `name=A`) are valid. The spec defines behaviour
   based solely on the first letter with no minimum length requirement. In a production
   system a minimum length constraint would be appropriate.
