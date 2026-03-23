# UC-003: Receive Stock

**As a** Warehouse Staff member, **I want to** record inbound shipments **so that** stock levels are updated accurately when goods arrive.

**Status:** Draft
**Date:** 2026-03-23

---

## Main Flow

- I navigate to the Receive Stock page.
- I select a product from a dropdown or search field.
- I enter the quantity received.
- I optionally enter a reference note (e.g., PO number or supplier name).
- I click "Receive".
- The system creates a stock event, increases the product's current stock by the received quantity, and shows a success notification with the new stock level.

---

## Business Rules

| ID | Rule |
|----|------|
| BR-01 | Quantity must be a positive integer (minimum 1) |
| BR-02 | A product must be selected before the form can be submitted |
| BR-03 | The product's current stock is increased by exactly the received quantity |
| BR-04 | A StockEvent of type RECEIVED is created with the quantity and optional reference |
| BR-05 | Any authenticated user can receive stock |

---

## Acceptance Criteria

- [ ] A product can be selected from the product dropdown
- [ ] Submitting with a valid quantity increases the product's stock level by that amount
- [ ] The success notification shows the product name and new stock level
- [ ] Submitting without selecting a product shows a validation error
- [ ] Submitting with zero or negative quantity shows a validation error
- [ ] A StockEvent record is created with type RECEIVED
- [ ] The form resets after a successful submission, ready for the next receipt

---

## Tests

> Write UI tests that verify the acceptance criteria above. See `architecture.md` § Testing for conventions.

- [ ] `ReceiveStockTest` — browserless tests covering successful receipt, validation errors, and stock level update

---

## UI / Routes

- Simple form layout: Product selector (ComboBox), Quantity (IntegerField), Reference (TextField, optional), "Receive" button.
- After successful submission, the form clears and a success notification appears.

| Route | Access | Notes |
|-------|--------|-------|
| `/receive` | authenticated | Vaadin Flow @Route |
