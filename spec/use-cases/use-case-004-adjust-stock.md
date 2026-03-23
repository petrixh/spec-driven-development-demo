# UC-004: Adjust Stock

**As a** Warehouse Manager, **I want to** correct stock discrepancies with a documented reason **so that** inventory records stay accurate and auditable.

**Status:** Draft
**Date:** 2026-03-23

---

## Main Flow

- I navigate to the Adjust Stock page.
- I select a product from a dropdown or search field. The current stock level is displayed.
- I enter the adjustment quantity (positive to add, negative to subtract).
- I enter a mandatory reason for the adjustment (e.g., "Damaged goods", "Cycle count correction").
- I click "Apply Adjustment".
- The system creates a stock event, updates the product's current stock, and shows a success notification with the old and new stock levels.

---

## Business Rules

| ID | Rule |
|----|------|
| BR-01 | Adjustment quantity must be a non-zero integer (positive or negative) |
| BR-02 | Reason is mandatory and must be at least 3 characters |
| BR-03 | An adjustment must not bring the stock below zero — if it would, show a validation error |
| BR-04 | A StockEvent of type ADJUSTMENT is created with the quantity and reason |
| BR-05 | Only users with the ADMIN role (Warehouse Manager) can adjust stock |

---

## Acceptance Criteria

- [ ] A product can be selected and its current stock is displayed
- [ ] A positive adjustment increases stock by the given amount
- [ ] A negative adjustment decreases stock by the given amount
- [ ] An adjustment that would bring stock below zero is rejected with a clear error message
- [ ] Submitting without a reason (or with fewer than 3 characters) shows a validation error
- [ ] Submitting with zero quantity shows a validation error
- [ ] A StockEvent record is created with type ADJUSTMENT and the reason
- [ ] The success notification shows old and new stock levels
- [ ] Non-admin users cannot access the Adjust Stock view

---

## Tests

> Write UI tests that verify the acceptance criteria above. See `architecture.md` § Testing for conventions.

- [ ] `AdjustStockTest` — browserless tests covering positive/negative adjustments, validation, below-zero guard, and access control

---

## UI / Routes

- Form layout: Product selector (ComboBox), Current Stock (read-only display), Adjustment Quantity (IntegerField), Reason (TextArea, required), "Apply Adjustment" button.
- Current stock updates when a product is selected.

| Route | Access | Notes |
|-------|--------|-------|
| `/adjust` | ADMIN | Vaadin Flow @Route |
