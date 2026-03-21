# UC-003: Product Administration

---

**As an** admin, **I want to** manage the product catalog **so that** the self-checkout terminal has up-to-date product information.

**Status:** Draft
**Date:** 2026-03-20

---

## Main Flow

- I navigate to `/admin/products` and am redirected to `/login` if not authenticated
- I log in with my admin username and password
- I see a list/grid of all products showing barcode, name, and price
- I can add a new product by entering barcode, name, and price
- I can edit an existing product's name and price
- I can delete a product
- Changes are saved immediately

---

## Business Rules

| ID | Rule |
|----|------|
| BR-01 | Only authenticated users with ADMIN role can access the product admin |
| BR-02 | Barcode must be unique — duplicate barcodes are rejected with an error |
| BR-03 | Name is required and must not be blank |
| BR-04 | Price is required and must be greater than zero |
| BR-05 | Deleting a product does not affect already-completed transactions (historical data is preserved) |

---

## Acceptance Criteria

- [ ] Unauthenticated users are redirected to `/login`
- [ ] Logging out ends the session and redirects to the public view
- [ ] After logout, navigating to an admin route redirects to `/login`
- [ ] Admin can see all products in a grid
- [ ] Admin can add a new product with barcode, name, and price
- [ ] Adding a product with a duplicate barcode shows an error
- [ ] Admin can edit a product's name and price
- [ ] Admin can delete a product
- [ ] Validation errors are shown for blank name or non-positive price

---

## Tests

> Write UI tests that verify the acceptance criteria above. See `architecture.md` § Testing for conventions.

- [ ] `ProductAdminTest` (Browserless test — Vaadin Flow view)
- [ ] `ProductServiceTest` (JUnit service test)
- [ ] Tests cover: CRUD operations, validation, duplicate barcode, access control, logout

---

## UI / Routes

Standard Vaadin Flow admin layout with a grid and a form.

- **Grid**: Columns for barcode, name, price, and action buttons (edit, delete)
- **Form**: Inline or side-panel form for adding/editing a product (barcode, name, price fields + save/cancel buttons)
- **Delete confirmation**: A confirmation dialog before deleting

| Route | Access | Notes |
|-------|--------|-------|
| `/admin/products` | ADMIN role | Vaadin Flow @Route view |
| `/login` | public | Vaadin LoginForm |
