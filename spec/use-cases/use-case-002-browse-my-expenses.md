# UC-002: Browse My Expenses

**As an** employee, **I want to** view my submitted expenses and their status **so that** I can track what has been approved, rejected, or is still pending.

**Status:** Implemented
**Date:** 2026-03-23

---

## Main Flow

- I navigate to the "My Expenses" page.
- I see a grid/table listing my expenses with columns: date, category, description, amount, and status.
- The list is sorted by date (most recent first).
- I can filter by status (Pending, Approved, Rejected) and/or date range.
- I see a summary row or section showing the total amount for the current filter.

---

## Business Rules

| ID | Rule |
|----|------|
| BR-01 | An employee can only see their own expenses. |
| BR-02 | Default sort order is by date descending. |
| BR-03 | The total reflects the currently filtered results. |

---

## Acceptance Criteria

- [ ] Grid displays columns: date, category, description, amount, status.
- [ ] Only the logged-in employee's expenses are shown.
- [ ] Filtering by status updates the grid and the total.
- [ ] Filtering by date range updates the grid and the total.
- [ ] Default sort is date descending.
- [ ] Status is visually distinguishable (e.g., color or badge).

---

## Tests

> Write UI tests that verify the acceptance criteria above. See `architecture.md` § Testing for conventions.

- [ ] `BrowseMyExpensesTest` — covers grid rendering, filtering, and totals

---

## UI / Routes

- Grid/table as the main component.
- Filter controls above the grid (status dropdown, date range picker).
- Total amount displayed below or above the grid.

| Route | Access | Notes |
|-------|--------|-------|
| `/my-expenses` | authenticated (Employee) | Vaadin @Route |
