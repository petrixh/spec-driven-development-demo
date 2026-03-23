# UC-001: Submit Expense

**As an** employee, **I want to** submit an expense with details and a receipt **so that** I can get reimbursed.

**Status:** Pending
**Date:** 2026-03-23

---

## Main Flow

- I navigate to the "Submit Expense" page.
- I see a form with fields for amount, date, category, description, and receipt upload.
- I fill in the amount and date of the expense.
- I select a category from a predefined list (e.g., Travel, Meals, Office Supplies, Other).
- I enter a short description.
- I optionally upload a receipt image.
- I click "Submit".
- The system creates the expense in "Pending" status and shows a confirmation message.
- I am redirected to my expenses list.

---

## Business Rules

| ID | Rule |
|----|------|
| BR-01 | Amount, date, category, and description are mandatory. |
| BR-02 | Amount must be a positive number. |
| BR-03 | Date cannot be in the future. |
| BR-04 | Receipt upload accepts JPEG and PNG only. |
| BR-05 | A newly submitted expense always starts in "Pending" status. |

---

## Acceptance Criteria

- [ ] Form displays all required fields: amount, date, category, description, receipt upload.
- [ ] Submitting a valid form creates an expense with status "Pending".
- [ ] Validation errors are shown for missing mandatory fields.
- [ ] Amount rejects zero, negative, and non-numeric values.
- [ ] Date rejects future dates.
- [ ] Receipt upload rejects non-image files.
- [ ] A success notification is shown after submission.

---

## Tests

> Write UI tests that verify the acceptance criteria above. See `architecture.md` § Testing for conventions.

- [ ] `SubmitExpenseTest` — covers form rendering, validation, and successful submission

---

## UI / Routes

- Single form layout with fields stacked vertically.
- Category is a dropdown/select.
- Receipt upload shows a file picker with accepted types.
- Submit button at the bottom.

| Route | Access | Notes |
|-------|--------|-------|
| `/submit-expense` | authenticated (Employee) | Vaadin @Route |
