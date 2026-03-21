# UC-006: Customer Administration

---

**As an** admin, **I want to** manage customers and their store card numbers **so that** the self-checkout terminal recognizes returning customers at payment.

**Status:** Draft
**Date:** 2026-03-21

---

## Main Flow

- I navigate to `/admin/customers` (redirected to `/login` if not authenticated)
- I see a list/grid of all customers showing name and card number
- I can add a new customer by entering a name and card number
- I can edit an existing customer's name and card number
- I can delete a customer
- Changes are saved immediately

---

## Business Rules

| ID | Rule |
|----|------|
| BR-01 | Only authenticated users with ADMIN role can access the customer admin |
| BR-02 | Card number (customerNumber) must be unique — duplicates are rejected with an error |
| BR-03 | Name is required and must not be blank |
| BR-04 | Card number is required and must not be blank |
| BR-05 | Deleting a customer does not affect already-completed transactions (historical data is preserved) |

---

## Acceptance Criteria

- [ ] Unauthenticated users are redirected to `/login`
- [ ] Admin can see all customers in a grid
- [ ] Admin can add a new customer with name and card number
- [ ] Adding a customer with a duplicate card number shows an error
- [ ] Admin can edit a customer's name and card number
- [ ] Admin can delete a customer
- [ ] Validation errors are shown for blank name or blank card number

---

## Tests

> Write UI tests that verify the acceptance criteria above. See `architecture.md` § Testing for conventions.

- [ ] `CustomerAdminTest` (Browserless test — Vaadin Flow view)
- [ ] `CustomerServiceTest` (JUnit service test)
- [ ] Tests cover: CRUD operations, validation, duplicate card number, access control

---

## UI / Routes

Standard Vaadin Flow admin layout with a grid and a form, following the same pattern as UC-003 (Product Administration).

- **Grid**: Columns for name, card number, and action buttons (edit, delete)
- **Form**: Inline or side-panel form for adding/editing a customer (name, card number fields + save/cancel buttons)
- **Delete confirmation**: A confirmation dialog before deleting
- **Admin navigation**: The admin root view (`/admin`) and layout navigation must include a link to this view

| Route | Access | Notes |
|-------|--------|-------|
| `/admin/customers` | ADMIN role | Vaadin Flow @Route view, uses shared AdminLayout |
