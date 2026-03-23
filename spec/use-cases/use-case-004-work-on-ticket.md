# UC-004: Work on a Ticket

**As an** agent, **I want to** view ticket details, add comments, and change status **so that** I can resolve customer issues.

**Status:** Draft
**Date:** 2026-03-23

---

## Main Flow

- I click on a ticket from the queue to open its detail view.
- I see the full ticket information: title, description, category, priority, status, created by, assigned to, and dates.
- I see a chronological list of all comments on the ticket.
- I type a comment in the text area and click "Add Comment". The comment appears in the list with my name and timestamp.
- I change the ticket status using a dropdown (e.g., OPEN → IN_PROGRESS).
- The status updates immediately and is reflected in the queue.

---

## Business Rules

| ID | Rule |
|----|------|
| BR-01 | Only agents (ADMIN role) can change ticket status. |
| BR-02 | Only agents can add comments. |
| BR-03 | Status transitions follow the allowed flow: OPEN → IN_PROGRESS → RESOLVED → CLOSED, plus RESOLVED → OPEN (reopen). |
| BR-04 | CLOSED is a terminal state — no further status changes allowed. |
| BR-05 | Comments cannot be empty. |
| BR-06 | Comments are immutable once created — no editing or deleting. |
| BR-07 | Changing status to IN_PROGRESS automatically assigns the ticket to the current agent if unassigned. |

---

## Acceptance Criteria

- [ ] Detail view shows all ticket fields and comment history
- [ ] Agent can add a comment and it appears immediately with author and timestamp
- [ ] Empty comment submission is prevented
- [ ] Status dropdown only shows valid next statuses based on current status
- [ ] Status changes persist and are reflected in the queue
- [ ] Setting IN_PROGRESS auto-assigns unassigned tickets to the current agent
- [ ] CLOSED tickets do not allow status changes or new comments
- [ ] Only users with ADMIN role can access this view

---

## Tests

- [ ] `WorkOnTicketTest` — covers comment creation, status transitions, auto-assignment, closed ticket restrictions, access control

---

## UI / Routes

- Split layout: ticket details on top/left, comments below/right
- Comment section: list of comments (author, timestamp, text) with a text area and "Add Comment" button at the bottom
- Status dropdown in the ticket details section

| Route | Access | Notes |
|-------|--------|-------|
| `/admin/ticket/{id}` | `ADMIN` role | Vaadin Flow view, `@Route` |
