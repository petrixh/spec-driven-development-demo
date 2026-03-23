# UC-002: View My Tickets

**As a** customer, **I want to** see a list of my submitted tickets **so that** I can track their status.

**Status:** Draft
**Date:** 2026-03-23

---

## Main Flow

- I navigate to "My Tickets".
- I see a list of all tickets I have submitted, sorted by most recently updated.
- Each ticket shows: title, category, priority, status, and created date.
- I can filter the list by status.
- I click on a ticket to see its full details and comment history.
- On the detail view I see the ticket fields, current status, and a chronological list of comments.

---

## Business Rules

| ID | Rule |
|----|------|
| BR-01 | Customers only see tickets they created — never other users' tickets. |
| BR-02 | The list defaults to showing all statuses. |
| BR-03 | Tickets are sorted by updated date, newest first. |
| BR-04 | Customers cannot change ticket status or assignment. |

---

## Acceptance Criteria

- [ ] List shows only the logged-in customer's tickets
- [ ] Each row displays title, category, priority, status, and created date
- [ ] Filtering by status works correctly
- [ ] Clicking a ticket navigates to the detail view
- [ ] Detail view shows ticket fields and comment history in chronological order
- [ ] Empty state shown when customer has no tickets

---

## Tests

- [ ] `ViewMyTicketsTest` — covers list filtering, detail navigation, data isolation between customers

---

## UI / Routes

- List view: card or table layout showing ticket summary rows
- Detail view: read-only ticket fields at top, comments listed below in chronological order
- Status filter: dropdown or toggle buttons above the list

| Route | Access | Notes |
|-------|--------|-------|
| `/tickets` | authenticated | Hilla React view — ticket list |
| `/tickets/{id}` | authenticated | Hilla React view — ticket detail |
