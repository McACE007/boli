# CLAUDE.md — Backend

This file provides guidance to Claude Code (claude.ai/code) when working in the `backend/` directory.

## Build & Run Commands

All Maven and Docker commands must be run from `backend/`.

### Infrastructure (required first)
```bash
docker-compose up -d        # Start Kafka, Redis, Kafka UI
docker-compose down
```

### Build (Maven multi-module)
```bash
mvn clean install                              # Build all modules
mvn clean install -DskipTests
mvn clean install -pl services/user-service    # Build single service
```

### Run services (must start in this order)
```bash
mvn spring-boot:run -pl services/config-server
mvn spring-boot:run -pl services/discovery-service
mvn spring-boot:run -pl services/user-service
mvn spring-boot:run -pl services/auction-service
mvn spring-boot:run -pl services/bidding-service
mvn spring-boot:run -pl services/api-gateway
```

### Notification service (Node.js)
```bash
cd services/notification-service
npm install
node index.js
```

### Tests
```bash
mvn test
mvn test -pl services/user-service
```

## Architecture

### Service Map

| Service | Port | Role |
|---|---|---|
| config-server | 8888 | Spring Cloud Config — serves all service configs from `services/config-server/src/main/resources/configurations/` |
| discovery-service | 8761 | Eureka server — all Java services register here |
| api-gateway | 8084 | Spring Cloud Gateway — single entry point, routes to services via Eureka (`lb://SERVICE-NAME`) |
| user-service | 8090 | User accounts, auth, JWT issuance |
| auction-service | 8091 | Auction lifecycle, Kafka producer (`auction-events`) |
| bidding-service | 8092 | Bid validation, Kafka consumer (`auction-events`) + producer (`bid-events`), Redis cache |
| notification-service | 8093 | Node.js SSE server, Kafka consumer (`bid-events`) |

**Infrastructure:** PostgreSQL (5432), Redis (6379), Kafka (9092), Kafka UI (8080).

### Communication Patterns

**Sync (REST):** Internal endpoints (`/internal/**`) handle service-to-service calls — e.g., Bidding Service calls Auction Service at `/internal/auctions/{id}/rules` to validate bids. These routes are protected by `InternalAuthFilter`.

**Async (Kafka):**
- `auction-events` topic: Auction Service → Bidding Service (AUCTION_STARTED populates Redis cache; AUCTION_ENDED marks it closed)
- `bid-events` topic: Bidding Service → Notification Service (BID_PLACED triggers SSE broadcast)

**Real-time push:** Notification Service maintains an in-memory client map keyed by `auctionId`; the SSE endpoint is `GET /api/notifications/stream/:auctionId`.

### Shared `common` Module

`common/` is a shared library (artifact `com.boli:common`) depended on by all services. It contains:
- `ApiResponse<T>` / `PageResponse<T>` — standard response wrappers
- `JwtUtil` — JWT generation/validation (JJWT 0.13.0)
- `ResponseBuilder` — helper for consistent responses
- Shared enums: `AuctionStatus`, `AuctionEventType`, `BidEventType`, `RoleType`, `UserStatus`

### Centralized Configuration

All service configs are served by config-server. Secrets and DB credentials live in `services/config-server/src/main/resources/configurations/`. Services pull config via `spring.config.import: optional:configserver:http://localhost:8888`. Profile-specific files follow the pattern `<service-name>-<profile>.yml`.

### Security

JWT-based auth is implemented in every Java service using Spring Security with custom filters. `UserDetailsServiceAutoConfiguration` is excluded in each service — security is handled via custom `SecurityConfig` classes. Role-based access control uses the `RoleType` enum. The JWT secret and expiration are in `configurations/application.yaml` (served by config-server).

### Database

All three data services (user, auction, bidding) connect to the same PostgreSQL database `boli` (`jdbc:postgresql://localhost:5432/boli`, user: `postgres`, password: `root`). Hibernate `ddl-auto: update` is active — schema evolves automatically. All entities use `Instant` (not `LocalDateTime`) for timestamps with JPA auditing.

### Redis

Bidding Service uses Redis via `AuctionCacheService` to cache active auction state (starting price, minimum increment) for fast bid validation without hitting the database on every bid.
