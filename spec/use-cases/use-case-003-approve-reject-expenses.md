# UC-003: Approve or Reject Expenses

**As a** manager, **I want to** review pending expenses and approve or reject them **so that** employees can be reimbursed promptly and invalid expenses are caught.

**Status:** Draft
**Date:** 2026-03-23

---

## Main Flow

- I navigate to the "Review Expenses" page.
- I see a grid of all pending expenses with columns: employee name, date, category, description, amount, and receipt indicator.
- I select an expense to review its details, including the receipt image if attached.
- I click "Approve" or "Reject".
- If rejecting, I enter a comment explaining the reason.
- The system updates the expense status and shows a confirmation.
- The expense disappears from the pending list.

---

## Business Rules

| ID | Rule |
|----|------|
| BR-01 | Only managers can access this view. |
| BR-02 | A rejection requires a comment; approval does not. |
| BR-03 | Once approved or rejected, the decision cannot be changed. |
| BR-04 | The review list shows only expenses with status "Pending". |

---

## Acceptance Criteria

- [ ] Grid shows only pending expenses.
- [ ] Selecting an expense shows its full details and receipt image.
- [ ] Approve updates the status to "Approved".
- [ ] Reject requires a comment and updates the status to "Rejected".
- [ ] Attempting to reject without a comment shows a validation error.
- [ ] Non-manager users cannot access this view.

---

## Tests

> Write UI tests that verify the acceptance criteria above. See `architecture.md` § Testing for conventions.

- [ ] `ApproveRejectExpensesTest` — covers approval flow, rejection with comment, and access control

---

## UI / Routes

- Grid on the left or top showing pending expenses.
- Detail panel or dialog showing expense details and receipt.
- Approve and Reject buttons in the detail view.
- Reject shows a comment text area before confirming.

| Route | Access | Notes |
|-------|--------|-------|
| `/review-expenses` | authenticated (Manager) | Vaadin @Route |
