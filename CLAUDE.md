# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Repository Structure

```
boli/
  backend/    ← Spring Boot microservices + Node.js notification service
  frontend/   ← React.js SPA (Vite, Tailwind, Framer Motion)
```

Each subdirectory has its own `CLAUDE.md` with area-specific guidance.

## Backend

An auction/bidding platform built as Spring Boot microservices. Services communicate via Kafka for async events and REST (`/internal/**`) for sync calls. Real-time bid notifications are pushed to browsers via Server-Sent Events from a Node.js service.

See `backend/CLAUDE.md` for build commands, service map, and architecture details.

## Frontend

React 19 SPA with a dark web3 design (Tailwind CSS, Framer Motion). Connects to the backend API gateway at `http://localhost:8084` via a Vite dev proxy. JWT stored in `localStorage`; decoded with `jwt-decode` for user display. Real-time bid updates consumed via `EventSource` (SSE).

Run with `cd frontend && npm run dev` — starts on `http://localhost:5173`.

See `frontend/README.md` for full setup, page map, and architecture notes.
