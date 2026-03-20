# UC-002: Employee Mode

---

**As an** employee, **I want to** unlock additional controls at the self-checkout terminal **so that** I can assist customers with tasks like cash payment.

**Status:** Draft
**Date:** 2026-03-20

---

## Main Flow

- A customer presses the "Call Employee" button on the checkout view
- A visible indicator shows that an employee has been called
- The employee arrives and sees a code input field (numeric keypad or text input)
- The employee enters the secret code `112233`
- The view enters employee mode, showing additional controls
- In employee mode, a "Paid by Cash" button and a "Select Customer" button are visible
- The employee presses "Paid by Cash" to finalize the current transaction as a cash payment
- The transaction is marked as completed with payment method "cash"
- Alternatively, the employee presses "Select Customer" which opens a customer selection dialog
- The employee searches for or selects a customer by name or customer number
- The transaction is marked as completed with payment method "card" and linked to the selected customer (this is the same as the customer scanning their own card)
- A confirmation is shown and the view resets for the next customer
- Employee mode also has a "Exit Employee Mode" button to return to normal customer mode

---

## Business Rules

| ID | Rule |
|----|------|
| BR-01 | The secret code is `112233` (hard-coded) |
| BR-02 | Entering a wrong code shows an error message and does not unlock employee mode |
| BR-03 | "Paid by Cash" is only available in employee mode |
| BR-04 | "Paid by Cash" requires at least one item in the transaction (same as regular pay) |
| BR-05 | Employee mode persists until explicitly exited or the transaction is completed |
| BR-06 | After a cash or card payment completes, employee mode is deactivated and the view resets |
| BR-12 | "Select Customer" is only available in employee mode |
| BR-13 | "Select Customer" requires selecting a customer from the customer list before completing |
| BR-14 | The customer selection allows searching by name or customer number |
| BR-15 | Selecting a customer completes the transaction as a card payment linked to that customer |

---

## Acceptance Criteria

- [ ] "Call Employee" button is always visible on the checkout view
- [ ] Pressing "Call Employee" shows a code input field
- [ ] Entering `112233` activates employee mode
- [ ] Entering a wrong code shows an error
- [ ] "Paid by Cash" button is visible only in employee mode
- [ ] "Paid by Cash" completes the transaction with payment method "cash"
- [ ] View resets after cash payment and employee mode is deactivated
- [ ] "Exit Employee Mode" returns to normal customer mode
- [ ] "Select Customer" button is visible only in employee mode
- [ ] "Select Customer" opens a customer selection dialog
- [ ] Employee can search customers by name or customer number
- [ ] Selecting a customer and confirming completes the transaction as a card payment linked to that customer

---

## Tests

> Write UI tests that verify the acceptance criteria above. See `architecture.md` § Testing for conventions.

- [ ] `EmployeeModeTest` (Browserless test — Vaadin Flow view)
- [ ] Tests cover: call employee flow, correct code entry, wrong code rejection, cash payment, select customer, mode exit

---

## UI / Routes

Employee mode is part of the self-checkout view (no separate route).

- **"Call Employee" button**: Always visible on the checkout view, styled distinctly (e.g., secondary/outlined)
- **Code input**: Appears as a dialog/overlay when "Call Employee" is pressed, with a numeric input and submit button
- **Employee mode indicator**: A visible badge or banner showing "Employee Mode" when active
- **"Paid by Cash" button**: Appears alongside the "Pay" button when in employee mode
- **"Select Customer" button**: Appears in employee mode, opens a customer selection dialog with search by name/number
- **"Exit Employee Mode" button**: Visible when in employee mode
- See also UC-005 for printer controls available in employee mode

| Route | Access | Notes |
|-------|--------|-------|
| `/` | public | Same Vaadin Flow checkout view — employee mode is a state, not a separate route |
