<div align="center">

<br />

```
██████╗  ██████╗ ██╗     ██╗
██╔══██╗██╔═══██╗██║     ██║
██████╔╝██║   ██║██║     ██║
██╔══██╗██║   ██║██║     ██║
██████╔╝╚██████╔╝███████╗██║
╚═════╝  ╚═════╝ ╚══════╝╚═╝
```

### Real-Time Auction Platform

*Bid. Win. Own.*

<br />

![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.3.5-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![React](https://img.shields.io/badge/React-19-61DAFB?style=for-the-badge&logo=react&logoColor=black)
![Kafka](https://img.shields.io/badge/Apache_Kafka-231F20?style=for-the-badge&logo=apache-kafka&logoColor=white)
![Redis](https://img.shields.io/badge/Redis-FF4438?style=for-the-badge&logo=redis&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-4169E1?style=for-the-badge&logo=postgresql&logoColor=white)

<br />

</div>

---

## What is Boli?

Boli is a full-stack, event-driven auction platform where users can create auctions, place competitive bids, and receive live price updates in real time — without ever refreshing the page.

The backend is a Spring Boot microservices system connected by Kafka for async event flow and Eureka for service discovery. The frontend is a dark-themed React SPA with web3-inspired animations and live bid feeds powered by Server-Sent Events.

---

## Features

| Feature | Description |
|---|---|
| **Live Bid Feed** | Browser receives bid updates via SSE the instant they are placed — no polling |
| **Smart Bid Validation** | Minimum increment rules enforced using Redis-cached auction state |
| **JWT Authentication** | Stateless, role-based auth (USER / ADMIN) across all services |
| **Auction Lifecycle** | Scheduler automatically transitions auctions: SCHEDULED → LIVE → ENDED |
| **Event-Driven Architecture** | Kafka decouples auction events from bid processing and notifications |
| **API Gateway** | Single entry point routes all traffic; handles JWT validation centrally |
| **Service Discovery** | Eureka lets services find each other dynamically with load balancing |
| **Centralized Config** | Spring Cloud Config Server — one place to manage all service configs |

---

## Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                        Browser (React SPA)                  │
│              REST + SSE  ▲                                  │
└──────────────────────────┼──────────────────────────────────┘
                           │
┌──────────────────────────▼──────────────────────────────────┐
│                    API Gateway  :8084                        │
│         JWT validation · routing · CORS · load balance      │
└──┬───────────┬───────────┬───────────────────────┬──────────┘
   │           │           │                       │
   ▼           ▼           ▼                       ▼
:8090       :8091       :8092                   :8093
User        Auction     Bidding              Notification
Service     Service     Service               Service
  │            │           │  (Node.js)          ▲
  │            │           │                     │
  │     Kafka: auction-events                     │
  │            └───────────►──────────────────────│
  │                        │                      │
  │                   Kafka: bid-events            │
  │                        └──────────────────────►
  │
  ├── PostgreSQL :5432  (users, auctions, bids — shared DB)
  ├── Redis :6379        (active auction cache for bid validation)
  └── Eureka :8761 · Config Server :8888  (infrastructure services)
```

### Event Flow

```
User places bid
      │
      ▼
Bidding Service ──► validates against Redis cache ──► saves to PostgreSQL
      │
      └──► publishes BID_PLACED ──► [bid-events topic]
                                          │
                                          ▼
                                  Notification Service
                                          │
                                          └──► SSE push to all connected browsers
```

```
Auction reaches start/end time
      │
      ▼
Auction Scheduler ──► updates status ──► publishes AUCTION_STARTED / AUCTION_ENDED
                                                │
                                                ▼
                                        [auction-events topic]
                                                │
                                                ▼
                                        Bidding Service
                                                │
                                     ┌──────────┴──────────┐
                                  STARTED                ENDED
                                     │                     │
                               populate Redis        evict Redis
                               (unlock bidding)    (close bidding)
```

---

## Tech Stack

### Backend

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 3.3.5 |
| API Gateway | Spring Cloud Gateway (reactive) |
| Service Discovery | Netflix Eureka |
| Config Management | Spring Cloud Config Server |
| Message Broker | Apache Kafka (KRaft — no ZooKeeper) |
| Cache | Redis |
| Database | PostgreSQL + Spring Data JPA |
| Authentication | Spring Security + JWT (JJWT 0.13) |
| Build | Maven (multi-module) |
| Notification | Node.js / Express / KafkaJS |

### Frontend

| Layer | Technology |
|---|---|
| Framework | React 19 + Vite 8 |
| Routing | React Router v7 |
| Server State | TanStack Query v5 |
| Client State | Zustand |
| Animations | Framer Motion |
| Styling | Tailwind CSS v3 |
| HTTP | Axios |
| Real-time | Native `EventSource` (SSE) |
| Toasts | Sonner |

---

## Repository Structure

```
boli/
├── backend/
│   ├── docker-compose.yml          # Kafka + Redis + Kafka UI
│   ├── pom.xml                     # Maven parent POM
│   ├── common/                     # Shared library: DTOs, enums, JwtUtil
│   └── services/
│       ├── config-server/          # Spring Cloud Config (port 8888)
│       ├── discovery-service/      # Eureka server (port 8761)
│       ├── api-gateway/            # Gateway + JWT filter (port 8084)
│       ├── user-service/           # Auth + user management (port 8090)
│       ├── auction-service/        # Auction lifecycle + Kafka producer (port 8091)
│       ├── bidding-service/        # Bids + Redis + Kafka consumer (port 8092)
│       └── notification-service/   # Node.js SSE server (port 8093)
│
└── frontend/
    └── src/
        ├── api/                    # Axios modules (auth, auctions, bids)
        ├── store/                  # Zustand auth store
        ├── hooks/                  # useSSE, useCountdown
        ├── components/             # Navbar, AuctionCard, BidFeed, Background
        └── pages/                  # Landing, Login, Register, Auctions, AuctionDetail, Create, Profile
```

---

## Getting Started

### Prerequisites

- Java 21, Maven 3.9+
- Node.js 18+
- Docker & Docker Compose
- PostgreSQL 15+ running locally on port 5432

### 1 — Infrastructure

```bash
cd backend
docker-compose up -d          # Kafka · Redis · Kafka UI
```

### 2 — Database

```sql
CREATE DATABASE boli;
-- host: localhost:5432  user: postgres  password: root
```

### 3 — Backend services (in order)

```bash
cd backend
mvn spring-boot:run -pl services/config-server       # wait until ready
mvn spring-boot:run -pl services/discovery-service   # then open :8761
mvn spring-boot:run -pl services/user-service
mvn spring-boot:run -pl services/auction-service
mvn spring-boot:run -pl services/bidding-service
mvn spring-boot:run -pl services/api-gateway
```

```bash
# Notification service
cd backend/services/notification-service
npm install && node index.js
```

### 4 — Frontend

```bash
cd frontend
npm install
npm run dev                   # http://localhost:5173
```

> All `/api` requests are proxied by Vite to the gateway at `http://localhost:8084` — no CORS setup needed.

---

## Service Map

| Service | Port | Purpose |
|---|---|---|
| API Gateway | 8084 | Single entry point for all client traffic |
| User Service | 8090 | Registration, login, JWT issuance |
| Auction Service | 8091 | Create and manage auctions |
| Bidding Service | 8092 | Place bids, Redis-backed validation |
| Notification Service | 8093 | SSE push for live bid updates |
| Discovery Service | 8761 | Eureka dashboard |
| Config Server | 8888 | Centralised configuration |
| Kafka UI | 8080 | Inspect topics and messages |

---

## API Overview

All requests go through the gateway on **`:8084`**.

```
POST   /api/auth/register         Create account
POST   /api/auth/login            Authenticate → returns JWT

GET    /api/me                    Current user profile

GET    /api/auctions              List auctions (filter: status, price, page)
POST   /api/auctions              Create auction
GET    /api/auctions/:id          Auction detail

POST   /api/bids                  Place a bid

GET    /api/notifications/stream/:auctionId   SSE stream for live bids
```

---

## Documentation

| Document | Location |
|---|---|
| Backend setup & architecture | [`backend/README.md`](backend/README.md) |
| Frontend setup & architecture | [`frontend/README.md`](frontend/README.md) |

---

<div align="center">

Built with Java, Spring Boot, React, Kafka, Redis, and PostgreSQL.

</div>
