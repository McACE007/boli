# Boli — Backend

Spring Boot microservices backend for the Boli auction platform. Handles user auth, auction lifecycle, bid processing, and real-time notifications.

---

## Table of Contents

- [Tech Stack](#tech-stack)
- [Prerequisites](#prerequisites)
- [Architecture Overview](#architecture-overview)
- [Project Structure](#project-structure)
- [Setup & Running](#setup--running)
  - [1. Start Infrastructure](#1-start-infrastructure)
  - [2. Set Up PostgreSQL](#2-set-up-postgresql)
  - [3. Start Spring Boot Services](#3-start-spring-boot-services)
  - [4. Start Notification Service](#4-start-notification-service)
- [Service Reference](#service-reference)
- [API Gateway Routes](#api-gateway-routes)
- [Environment & Configuration](#environment--configuration)
- [Building](#building)

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 3.3.5 |
| Service Discovery | Spring Cloud Netflix Eureka |
| API Gateway | Spring Cloud Gateway (reactive) |
| Config Management | Spring Cloud Config Server |
| Messaging | Apache Kafka (KRaft mode) |
| Caching | Redis |
| Database | PostgreSQL |
| Auth | Spring Security + JWT (JJWT 0.13.0) |
| Build Tool | Apache Maven (multi-module) |
| Notification Service | Node.js 18+ / Express + KafkaJS |

---

## Prerequisites

Make sure the following are installed before starting:

| Tool | Version | Notes |
|---|---|---|
| Java | 21 | Must be JDK 21, not just JRE |
| Maven | 3.9+ | Or use the `./mvnw` wrapper inside any service |
| Node.js | 18+ | For the notification service |
| Docker & Docker Compose | Latest | For Kafka and Redis |
| PostgreSQL | 15+ | Must be running locally on port 5432 |

---

## Architecture Overview

```
Client
  │
  ▼
API Gateway (8084)          ← single entry point for all requests
  │
  ├── /api/users/**     →   User Service (8090)       ← auth, JWT, accounts
  ├── /api/auctions/**  →   Auction Service (8091)     ← auction lifecycle
  ├── /api/bids/**      →   Bidding Service (8092)     ← bid validation + Redis
  └── /api/notifications/** → Notification Service (8093)  ← Node.js SSE
  
All Java services register with:
  Discovery Service / Eureka (8761)

All Java services pull config from:
  Config Server (8888)

Async event flow:
  Auction Service ──[auction-events]──► Bidding Service
                                            │
                                            └──[bid-events]──► Notification Service ──► Browser (SSE)
```

---

## Project Structure

```
backend/
├── docker-compose.yml              # Kafka + Redis + Kafka UI
├── pom.xml                         # Parent Maven POM
├── common/                         # Shared library (DTOs, enums, JwtUtil)
└── services/
    ├── config-server/              # Spring Cloud Config Server
    │   └── src/main/resources/
    │       └── configurations/     # All service config files live here
    ├── discovery-service/          # Eureka Server
    ├── api-gateway/                # Spring Cloud Gateway
    ├── user-service/               # User management & auth
    ├── auction-service/            # Auction lifecycle + Kafka producer
    ├── bidding-service/            # Bid processing + Redis + Kafka consumer/producer
    └── notification-service/       # Node.js SSE + Kafka consumer
```

---

## Setup & Running

Services must be started in the exact order below. Each step depends on the previous one being healthy.

### 1. Start Infrastructure

From the `backend/` directory:

```bash
docker-compose up -d
```

This starts:
- **Kafka** on `localhost:9092` (KRaft mode, no ZooKeeper)
- **Redis** on `localhost:6379`
- **Kafka UI** on `http://localhost:8080`

Verify everything is up:

```bash
docker-compose ps
```

All three containers should show `running`. Wait ~15 seconds for Kafka to fully initialize before starting any service that uses it.

---

### 2. Set Up PostgreSQL

PostgreSQL must be running locally (not in Docker). Create the database:

```sql
CREATE DATABASE boli;
```

Connection details used by all services:

| Property | Value |
|---|---|
| Host | `localhost` |
| Port | `5432` |
| Database | `boli` |
| Username | `postgres` |
| Password | `root` |

> All three data services (user, auction, bidding) share this single database. Hibernate `ddl-auto: update` will create/update tables automatically on first startup — no migration scripts needed.

---

### 3. Start Spring Boot Services

All commands run from the `backend/` directory. Open a separate terminal for each service.

> **Order matters.** Config Server and Discovery Service must be fully started before any downstream service.

#### Step 1 — Config Server
```bash
mvn spring-boot:run -pl services/config-server
```
Wait until you see `Started ConfigServerApplication` in the logs, then open a new terminal.

#### Step 2 — Discovery Service (Eureka)
```bash
mvn spring-boot:run -pl services/discovery-service
```
Wait until started, then verify the Eureka dashboard is accessible at `http://localhost:8761`.

#### Step 3 — User Service
```bash
mvn spring-boot:run -pl services/user-service
```

#### Step 4 — Auction Service
```bash
mvn spring-boot:run -pl services/auction-service
```

#### Step 5 — Bidding Service
```bash
mvn spring-boot:run -pl services/bidding-service
```

#### Step 6 — API Gateway
```bash
mvn spring-boot:run -pl services/api-gateway
```

After each Spring Boot service starts, it will register itself with Eureka. You can watch registrations appear at `http://localhost:8761`.

---

### 4. Start Notification Service

```bash
cd services/notification-service
npm install       # first time only
node index.js
```

The service starts on `http://localhost:8093`.

---

## Service Reference

| Service | Port | URL | Purpose |
|---|---|---|---|
| Config Server | 8888 | `http://localhost:8888` | Serves config to all services |
| Discovery Service | 8761 | `http://localhost:8761` | Eureka dashboard |
| API Gateway | 8084 | `http://localhost:8084` | Single entry point |
| User Service | 8090 | `http://localhost:8090` | Users & auth |
| Auction Service | 8091 | `http://localhost:8091` | Auctions |
| Bidding Service | 8092 | `http://localhost:8092` | Bids |
| Notification Service | 8093 | `http://localhost:8093` | SSE push |
| Kafka UI | 8080 | `http://localhost:8080` | Kafka topic browser |

---

## API Gateway Routes

All client requests should go through the gateway on port **8084**.

| Prefix | Routes To | Service |
|---|---|---|
| `/api/users/**` | `lb://USER-SERVICE` | User Service |
| `/api/auctions/**` | `lb://AUCTION-SERVICE` | Auction Service |
| `/api/bids/**` | `lb://BIDDING-SERVICE` | Bidding Service |
| `/api/notifications/**` | `http://localhost:8093` | Notification Service (Node.js, not in Eureka) |

The gateway uses Eureka load-balancing (`lb://`) for all Java services. CORS is globally enabled for all origins.

### Real-time Notifications (SSE)

To subscribe to live bid updates for an auction:

```
GET http://localhost:8084/api/notifications/stream/{auctionId}
```

The connection stays open and pushes a JSON event every time a bid is placed on that auction.

---

## Environment & Configuration

Configuration is centralized in Config Server. No `.env` files are needed — all service-specific config files live in:

```
services/config-server/src/main/resources/configurations/
```

| File | Used By |
|---|---|
| `application.yaml` | All services (JWT secret, Eureka settings) |
| `user-service.yml` | User Service |
| `auction-service.yml` / `auction-service-dev.yml` | Auction Service |
| `bidding-service-dev.yml` | Bidding Service |
| `discovery-service.yml` | Discovery Service |

Services fetch their config on startup via:
```
spring.config.import: optional:configserver:http://localhost:8888
```

If Config Server is unreachable at startup, `optional:` means the service will fall back to its own `application.yml` instead of failing.

---

## Building

```bash
# From backend/

# Build everything
mvn clean install

# Build without running tests
mvn clean install -DskipTests

# Build a single service
mvn clean install -pl services/user-service

# Run tests
mvn test
mvn test -pl services/user-service
```
