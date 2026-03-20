# Project Context

> High-level context for the project: the problem being solved, who it's for, what's in scope, and what constraints apply.

## 1. Vision

A cinema web application that lets moviegoers browse what's playing, pick a showtime, choose their seats, and buy tickets — all in a smooth, modern interface. On the other side, cinema staff use an admin panel to manage the movie catalogue and schedule shows. The goal is a self-contained demo app that covers the full ticket-buying lifecycle from both the customer and operator perspective.

## 2. Users

| Role | Description | Capabilities |
|------|-------------|--------------|
| **Moviegoer** (public, unauthenticated) | Anyone visiting the site | Browse movies, view showtimes, select seats, purchase tickets |
| **Admin** (authenticated, role `ADMIN`) | Cinema staff managing operations | Create/edit/delete movies, schedule shows, view ticket sales |

## 3. Constraints

- **Database:** H2 (embedded, file-based in dev; in-memory for tests)
- **Sample data:** Pre-loaded movies with poster images from `posters/` directory
- **Authentication:** Spring Security — admin routes require login; public routes are open
- **No external payment provider** — ticket purchase is simulated (mark as sold, no real payment)
- **Single-cinema model** — one venue with a fixed set of screening rooms

> For technology stack and application structure details, see [`architecture.md`](architecture.md).

---

# Related Documents

- [Spec README](README.md) — process overview and workflow
- [Architecture](architecture.md) — technology stack and application structure
- [Use Case Template](use-cases/use-case-template.md) — template for feature specifications
- [Verification](verification.md) — visual verification checklists
