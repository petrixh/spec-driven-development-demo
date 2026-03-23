# UC-005: Dashboard

**As a** warehouse team member, **I want to** see an at-a-glance overview of inventory health **so that** I can spot problems quickly without digging through individual records.

**Status:** Draft
**Date:** 2026-03-23

---

## Main Flow

- I open the application (or navigate to the Dashboard).
- I see summary cards showing: Total Products, Total Stock Value, Low-Stock Items count, Out-of-Stock Items count.
- Below the cards, I see a "Low-Stock Alerts" section listing products at or below their reorder point.
- Below that, I see a "Recent Activity" section showing the most recent stock events (receives and adjustments).

---

## Business Rules

| ID | Rule |
|----|------|
| BR-01 | Total Stock Value = sum of (currentStock × unitPrice) across all products |
| BR-02 | Low-Stock Items count = number of products where currentStock > 0 and currentStock ≤ reorderPoint |
| BR-03 | Out-of-Stock Items count = number of products where currentStock = 0 |
| BR-04 | Low-Stock Alerts list shows at most 10 products, ordered by most critical first (lowest stock relative to reorder point) |
| BR-05 | Recent Activity shows the last 10 stock events, newest first |
| BR-06 | Any authenticated user can view the dashboard |

---

## Acceptance Criteria

- [ ] The dashboard displays four summary cards: Total Products, Total Stock Value, Low-Stock Items, Out-of-Stock Items
- [ ] Total Stock Value is calculated correctly as sum of (stock × price)
- [ ] Low-Stock Alerts section lists products at or below their reorder point
- [ ] Recent Activity section shows the latest stock events with product name, type, quantity, and timestamp
- [ ] The dashboard is the default landing page after login
- [ ] Both admin and non-admin users can view the dashboard

---

## Tests

> Write UI tests that verify the acceptance criteria above. See `architecture.md` § Testing for conventions.

- [ ] `DashboardTest` — browserless tests covering summary card values, low-stock alerts, recent activity, and access control

---

## UI / Routes

- Top section: four summary cards in a horizontal row (stacked on mobile).
- Middle section: "Low-Stock Alerts" — a compact grid or list with columns: Name, SKU, Current Stock, Reorder Point.
- Bottom section: "Recent Activity" — a list or grid with columns: Timestamp, Product, Event Type, Quantity, Reference/Reason.

| Route | Access | Notes |
|-------|--------|-------|
| `/` | authenticated | Vaadin Flow @Route, default landing page |
