# UC-003: Manage Ticket Queue

**As an** agent, **I want to** browse and manage all open tickets **so that** I can triage and pick up work.

**Status:** Draft
**Date:** 2026-03-23

---

## Main Flow

- I navigate to the ticket queue in the admin area.
- I see a grid of all tickets across all customers.
- The grid shows: ID, title, category, priority, status, created by, assigned to, and created date.
- I can filter by status, category, and priority.
- I can sort by any column.
- I click "Assign to me" on an unassigned ticket to take ownership.
- The grid updates to show me as the assigned agent.

---

## Business Rules

| ID | Rule |
|----|------|
| BR-01 | Agents see all tickets, regardless of who created them. |
| BR-02 | The queue defaults to showing non-closed tickets (OPEN, IN_PROGRESS, RESOLVED). |
| BR-03 | An agent can assign any unassigned ticket to themselves. |
| BR-04 | An already-assigned ticket can be reassigned to a different agent. |
| BR-05 | Grid supports sorting by any column. |

---

## Acceptance Criteria

- [ ] Grid displays all tickets with the correct columns
- [ ] Default filter excludes CLOSED tickets
- [ ] Filtering by status, category, and priority works
- [ ] Column sorting works on all columns
- [ ] "Assign to me" assigns the ticket to the logged-in agent
- [ ] Already-assigned tickets show the current assignee
- [ ] Only users with ADMIN role can access this view

---

## Tests

- [ ] `ManageTicketQueueTest` — covers grid rendering, filtering, sorting, assignment, access control

---

## UI / Routes

- Grid layout with column headers, filtering controls above the grid
- "Assign to me" button in each row (or as a row action)
- Filters: status multi-select, category select, priority select

| Route | Access | Notes |
|-------|--------|-------|
| `/admin/queue` | `ADMIN` role | Vaadin Flow view, `@Route` |
