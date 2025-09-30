# Transport Booking (Spring Boot, Java 17, Maven 3.9.x)

This project contains two Spring Boot microservices:

- **vehicle-service** (port 8081) — exposes `/vehicles` and `/routes` (+ helper `/routes/{id}`)
- **ticket-service** (port 8082) — exposes `/login`, `/tickets`, `/tickets/{id}` and serves a Bootstrap UI (`/index.html`).

## Requirements
- Java 17.0.2 (or compatible Java 17)
- Maven 3.9.11
- MySQL server

## 1) Configure MySQL credentials
Each service has `src/main/resources/application.properties` with placeholders you can override by environment variables:

```
spring.datasource.url=jdbc:mysql://localhost:3306/${MYSQL_DB:transport_vehicle}?createDatabaseIfNotExist=true...
spring.datasource.username=${MYSQL_USER:root}
spring.datasource.password=${MYSQL_PASSWORD:password}
```

Set values per service (they use different default DB names: `transport_vehicle`, `transport_ticket`).

**Option A (edit files):** Put your real username/password and DB name in each `application.properties`.

**Option B (env vars):**
- On Windows PowerShell:
  ```powershell
  $env:MYSQL_USER="root"
  $env:MYSQL_PASSWORD="yourpass"
  $env:MYSQL_DB="transport_vehicle"
  ```

## 2) Create the databases
You can let Hibernate auto-create if you leave `createDatabaseIfNotExist=true` in the JDBC URL, or create manually:

```sql
CREATE DATABASE transport_vehicle;
CREATE DATABASE transport_ticket;
```

## 3) Build & Run from VS Code
Install the **Java Extension Pack** and **Spring Boot Extension Pack** (recommended).

Open the root folder `transport-booking` in VS Code.

**Build all modules:**
```bash
mvn -v    # should show Maven 3.9.11 and Java 17
mvn clean install
```

**Run vehicle-service:**
```bash
cd vehicle-service
mvn spring-boot:run
# API: http://localhost:8081/vehicles and http://localhost:8081/routes
```

**Run ticket-service (in another terminal):**
```bash
cd ticket-service
mvn spring-boot:run
# UI:  http://localhost:8082/index.html
# API: POST http://localhost:8082/login, POST http://localhost:8082/tickets, GET http://localhost:8082/tickets/{id}
```

Seed users are created on start:
- `alice` / `password`
- `bob` / `password`

## 4) Booking flow
1. Open `http://localhost:8082/index.html` and log in (alice/password).
2. Go to **Search Vehicles** to view routes (fetched from `vehicle-service`).
3. Click **Book**, then confirm. The service checks capacity via OpenFeign calling `vehicle-service` route and vehicle capacity.
4. See **Ticket Confirmation** (GET `/tickets/{id}`), protected by JWT.

## Notes
- Package structure follows `controller`, `service`, `repository`, `model`, `config` for both services.
- JWT secret and security are simplified for demo purposes only.
- CORS is open for convenience during local development.
- If `vehicle-service` runs on a different host/port, set env var `VEHICLE_SERVICE_URL` for `ticket-service`.
