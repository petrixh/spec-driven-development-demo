# UC-004: Expense Dashboard

**As a** manager, **I want to** see a summary of expenses **so that** I can quickly understand spending patterns and pending workload.

**Status:** Draft
**Date:** 2026-03-23

---

## Main Flow

- I navigate to the "Dashboard" page.
- I see summary cards showing: total pending amount, total approved amount this month, and number of pending expenses.
- I see a breakdown of approved expenses by category (e.g., a bar or pie chart).
- The dashboard reflects current data.

---

## Business Rules

| ID | Rule |
|----|------|
| BR-01 | Only managers can access the dashboard. |
| BR-02 | "This month" refers to the current calendar month. |
| BR-03 | The category breakdown includes only approved expenses. |

---

## Acceptance Criteria

- [ ] Dashboard shows total pending amount.
- [ ] Dashboard shows total approved amount for the current month.
- [ ] Dashboard shows the count of pending expenses.
- [ ] A chart displays approved expenses broken down by category.
- [ ] Non-manager users cannot access this view.

---

## Tests

> Write UI tests that verify the acceptance criteria above. See `architecture.md` § Testing for conventions.

- [ ] `ExpenseDashboardTest` — covers summary cards, chart rendering, and access control

---

## UI / Routes

- Summary cards at the top.
- Chart below the cards.
- No interactive filtering needed for now.

| Route | Access | Notes |
|-------|--------|-------|
| `/dashboard` | authenticated (Manager) | Vaadin @Route |
