# Project Context

> High-level context for the project: the problem being solved, who it's for, what's in scope, and what constraints apply.

---

## 1. Problem Statement

Public transit riders need a fast way to buy tickets on the go. The current purchasing process is slow and clunky, forcing riders to navigate complex interfaces or wait in line at physical kiosks, often missing their bus or train as a result.

- Existing ticket purchase flow has too many steps, frustrating riders who are in a hurry
- No mobile-optimized experience — riders struggle to buy tickets from their phones
- Riders must choose between slow digital tools and physical queues, both of which waste time

---

## 2. Vision

A mobile-first web app where transit riders can buy tickets through a simple 4-page flow — no registration, no account, just pick and pay. The flow is:

1. **Browse** — view available tickets across transit modes (bus, train, metro, ferry)
2. **Detail** — see ticket details and select the number of tickets
3. **Summary + Payment** — review the order and enter credit card information
4. **Confirmation** — see ticket details with a UUID serving as a unique validation code

A supplementary **Lookup** page lets riders retrieve past purchase details by entering their UUID confirmation code.

This is a concept demo: ticket data is seeded into an in-memory H2 database, payment is simulated (credit card form only, no real processing), and no external services are called. The goal is a clean, functional prototype that demonstrates the purchase flow end to end.

---

## 3. Users

| Role | Description | Access Scope |
|------|-------------|--------------|
| **Rider** | General public transit user — no account required | Browse ticket types, purchase tickets, view confirmation |

---

## 4. Scope

> High-level capabilities, not detailed features (those go in use cases).

### In Scope
- 4-page purchase flow: Browse → Detail → Summary/Payment → Confirmation
- Purchase lookup: look up a past purchase by UUID confirmation code
- Browse tickets across transit modes (bus, train, metro, ferry)
- Ticket detail view with quantity selection
- Order summary with credit card entry form (simulated — no real processing)
- Confirmation page displaying ticket details and a UUID as validation code
- Seed ticket data in an embedded H2 database (no external data sources)
- Mobile-first responsive design

### Out of Scope
- User accounts, registration, or login
- Real payment processing or payment gateway integration
- Admin panel for managing ticket types or pricing
- QR codes, PDF tickets, or email delivery
- Route/schedule browsing or trip planning
- Payment methods other than credit card

---

## 5. Constraints

- Mobile-first design — must be fully usable on phone screens
- Concept demo — seed data in an embedded H2 database, no external APIs required
- Credit card only — single payment method, simulated (no real processing)
- No authentication — fully anonymous, no registration or login

> For technology stack and application structure details, see [`architecture.md`](architecture.md).

---

## 6. Assumptions

1. Ticket data (types, modes, pricing) is seeded into an in-memory H2 database on startup — simulates a real data layer
2. Users have a modern mobile browser with JavaScript enabled
3. All purchases are simulated — no real money changes hands, no payment validation
4. The UUID on the confirmation page serves as the unique ticket identifier/validation code

---

## 7. Risks

- Oversimplification — simulated payment may not uncover real-world UX issues; mitigate by designing the flow as if payment were real
- Mobile responsiveness gaps — Vaadin components may not render ideally on all phone sizes; mitigate with early mobile testing

---

## 8. Related Documents

- [Spec README](README.md) — process overview and workflow
- [Architecture](architecture.md) — technology stack and application structure
- [Design Specification](design.md) — shared visual components, responsive rules, and design asset index
- [Design System](../design/design-system.md) — full color palette, typography, component CSS, and layout diagrams
- [Design Screenshots](../design/screenshots/) — reference screenshots for all 4 screens (mobile + desktop)
- [Use Case Template](use-cases/use-case-template.md) — template for feature specifications
- [Verification](verification.md) — visual verification checklists
