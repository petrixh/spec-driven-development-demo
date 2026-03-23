# UC-001: Submit a Ticket

**As a** customer, **I want to** submit a support ticket **so that** I can get help with my issue.

**Status:** Draft
**Date:** 2026-03-23

---

## Main Flow

- I navigate to the ticket submission page.
- I see a form with fields: title, category (dropdown), priority (dropdown), and description (text area).
- I fill in all fields and click "Submit".
- The system creates the ticket with status `OPEN` and shows a success notification with the ticket number.
- I am redirected to my tickets list where the new ticket appears at the top.

---

## Business Rules

| ID | Rule |
|----|------|
| BR-01 | Title and description are mandatory. |
| BR-02 | Category must be one of: General, Technical, Billing, Access. |
| BR-03 | Priority must be one of: Low, Medium, High, Critical. |
| BR-04 | Priority defaults to Medium if not explicitly chosen. |
| BR-05 | The ticket's `createdBy` is automatically set to the logged-in user. |
| BR-06 | New tickets always start with status `OPEN`. |

---

## Acceptance Criteria

- [ ] Form displays all required fields (title, category, priority, description)
- [ ] Submitting with empty title or description shows validation errors
- [ ] Priority defaults to Medium
- [ ] Successful submission creates a ticket with status OPEN
- [ ] Success notification displays after submission
- [ ] User is redirected to the ticket list after submission

---

## Tests

- [ ] `SubmitTicketTest` — covers form rendering, validation, default priority, successful submission

---

## UI / Routes

- Form layout: vertical stack with title (text field), category (select), priority (select), description (text area), and a Submit button
- Validation errors appear inline below the relevant field

| Route | Access | Notes |
|-------|--------|-------|
| `/submit` | authenticated | Hilla React view, `@BrowserCallable` endpoint |
