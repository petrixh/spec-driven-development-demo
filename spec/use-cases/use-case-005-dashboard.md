# UC-005: Dashboard

**As an** agent or manager, **I want to** see a summary of ticket metrics **so that** I can understand workload and team performance.

**Status:** Draft
**Date:** 2026-03-23

---

## Main Flow

- I navigate to the dashboard in the admin area.
- I see summary cards showing: total open tickets, total in-progress tickets, total resolved (today), and total closed (today).
- Below the cards, I see a breakdown table of open tickets grouped by category.
- I also see a breakdown table of open tickets grouped by priority.

---

## Business Rules

| ID | Rule |
|----|------|
| BR-01 | "Open" count includes tickets with status OPEN. |
| BR-02 | "In Progress" count includes tickets with status IN_PROGRESS. |
| BR-03 | "Resolved today" counts tickets that moved to RESOLVED status today. |
| BR-04 | "Closed today" counts tickets that moved to CLOSED status today. |
| BR-05 | Category and priority breakdowns only count non-closed tickets (OPEN, IN_PROGRESS, RESOLVED). |

---

## Acceptance Criteria

- [ ] Dashboard displays four summary cards with correct counts
- [ ] Category breakdown table shows correct ticket counts per category
- [ ] Priority breakdown table shows correct ticket counts per priority
- [ ] Counts update when tickets are created or status changes
- [ ] Only users with ADMIN role can access the dashboard

---

## Tests

- [ ] `DashboardTest` — covers metric calculations, category/priority breakdowns, access control

---

## UI / Routes

- Top row: four summary stat cards (Open, In Progress, Resolved Today, Closed Today)
- Below: two side-by-side tables — tickets by category, tickets by priority
- Read-only view, no interactive controls

| Route | Access | Notes |
|-------|--------|-------|
| `/admin/dashboard` | `ADMIN` role | Vaadin Flow view, `@Route` |
