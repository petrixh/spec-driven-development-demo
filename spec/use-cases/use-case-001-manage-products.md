# UC-001: Manage Products

**As a** Warehouse Manager, **I want to** add, edit, and delete products in the catalog **so that** our inventory system reflects the actual items we stock.

**Status:** Draft
**Date:** 2026-03-23

---

## Main Flow

### Adding a product
- I navigate to the Products page.
- I click the "Add product" button.
- A form appears (inline or in a side panel) with fields: Name, SKU, Category, Unit Price, Reorder Point.
- I fill in the fields and click "Save".
- The product appears in the grid. A confirmation notification is shown.

### Editing a product
- I select a product in the grid.
- The form populates with the product's current values.
- I change one or more fields and click "Save".
- The grid updates. A confirmation notification is shown.

### Deleting a product
- I select a product in the grid.
- I click the "Delete" button.
- A confirmation dialog asks "Are you sure?".
- I confirm. The product is removed from the grid. A confirmation notification is shown.

---

## Business Rules

| ID | Rule |
|----|------|
| BR-01 | SKU must be unique across all products |
| BR-02 | Name, SKU, and Unit Price are mandatory fields |
| BR-03 | Unit Price must be a positive number |
| BR-04 | Reorder Point must be a non-negative integer (default 0) |
| BR-05 | A product with existing stock events cannot be deleted — it must be soft-deleted or the user must be warned |
| BR-06 | Only users with the ADMIN role (Warehouse Manager) can access this view |

---

## Acceptance Criteria

- [ ] A new product can be created with all required fields and appears in the grid
- [ ] Saving a product with a duplicate SKU shows a validation error
- [ ] Saving a product with missing mandatory fields shows validation errors
- [ ] Unit Price rejects zero and negative values
- [ ] An existing product can be selected, edited, and saved with updated values
- [ ] Deleting a product shows a confirmation dialog before removal
- [ ] A product with stock events cannot be deleted (or user is warned)
- [ ] Non-admin users cannot access the Manage Products view

---

## Tests

> Write UI tests that verify the acceptance criteria above. See `architecture.md` § Testing for conventions.

- [ ] `ManageProductsTest` — browserless tests covering create, edit, delete, validation, and access control

---

## UI / Routes

- Master-detail layout: grid on the left, form on the right (or below on mobile).
- Grid columns: Name, SKU, Category, Unit Price, Reorder Point, Current Stock.
- Form fields: Name (text), SKU (text), Category (text or combo box), Unit Price (number), Reorder Point (number).
- "Add product" button above the grid. "Save" and "Delete" buttons in the form.

| Route | Access | Notes |
|-------|--------|-------|
| `/products` | ADMIN | Vaadin Flow @Route, master-detail |
