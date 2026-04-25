# Boli — Frontend

React.js SPA for the Boli auction platform. Dark web3 aesthetic with real-time bid updates via SSE.

---

## Tech Stack

| Tool | Purpose |
|---|---|
| React 19 + Vite 8 | Framework & build |
| React Router v7 | Client-side routing |
| TanStack Query | Server state / data fetching |
| Zustand | Auth state (JWT) |
| Framer Motion | Animations & transitions |
| Tailwind CSS v3 | Styling |
| Axios | HTTP client |
| Sonner | Toast notifications |
| Lucide React | Icons |
| jwt-decode | Decode JWT claims for UI |

---

## Prerequisites

- Node.js 18+
- The backend must be running on `http://localhost:8084` (see `backend/README.md`)

---

## Setup & Running

```bash
cd frontend
npm install       # first time only
npm run dev       # starts on http://localhost:5173
```

Vite proxies all `/api/**` requests to `http://localhost:8084` — no CORS config needed.

## Build

```bash
npm run build     # outputs to dist/
npm run preview   # serve the production build locally
```

---

## Pages

| Route | Description | Auth required |
|---|---|---|
| `/` | Landing page | No |
| `/login` | Sign in | No |
| `/register` | Create account | No |
| `/auctions` | Browse & filter auctions | Yes |
| `/auctions/:id` | Auction detail + live bid feed | Yes |
| `/create` | Create a new auction | Yes |
| `/profile` | User profile | Yes |

---

## Architecture

```
src/
├── api/           # Axios functions per domain (auth, auctions, bids)
├── store/         # Zustand auth store — stores JWT, decodes user claims
├── hooks/
│   ├── useSSE.js       # EventSource hook for real-time bid events
│   └── useCountdown.js # Tick-based countdown timer
├── utils/format.js     # Currency, date, status helpers
├── components/
│   ├── layout/         # Navbar + Layout wrapper
│   ├── Background.jsx  # Animated gradient orbs (CSS + Framer Motion)
│   ├── AuctionCard.jsx # Card with countdown + status badge
│   └── BidFeed.jsx     # Animated live bid list
└── pages/              # One file per route
```

**Auth flow:** JWT stored in `localStorage`. Axios interceptor injects `Authorization: Bearer <token>` on every request. On 401 the interceptor clears the token and redirects to `/login`.

**Real-time bids:** `useSSE(auctionId)` opens a native `EventSource` to `/api/notifications/stream/{id}`. Incoming JSON events are prepended to a local `bids` array and animated in via Framer Motion `AnimatePresence`.
