# UC-001: Self-Checkout

---

**As a** customer, **I want to** scan products and pay with my store card at a self-checkout terminal **so that** I can complete my purchase quickly without waiting for a cashier.

**Status:** Draft
**Date:** 2026-03-20

---

## Main Flow

- I walk up to the self-checkout terminal and see the checkout view with an empty item list and a total of €0.00
- I scan a product barcode (the scanner types the barcode string and presses Enter)
- The scanned product appears in the item list with its name, price, and quantity
- I scan more products — each one is added to the list (if the same product is scanned again, the quantity increments)
- The running total updates after each scan
- When I'm done, I press the "Pay" button
- The view prompts me to scan my store card
- I scan my store card barcode
- The transaction is completed and a confirmation is shown
- If a Bluetooth thermal printer is connected (see UC-002), a receipt is printed; otherwise a notification shows "No receipt can be printed."
- After a short delay (or pressing a button), the view resets for the next customer

---

## Business Rules

| ID | Rule |
|----|------|
| BR-01 | Only known product barcodes are accepted; unknown barcodes show a brief error message |
| BR-02 | Scanning the same product multiple times increments its quantity rather than adding a duplicate line |
| BR-03 | The total is always the sum of (price × quantity) for all items |
| BR-04 | The "Pay" button is disabled when the item list is empty |
| BR-05 | After pressing "Pay", only a store card scan is accepted (product scans are ignored until payment completes or is cancelled) |
| BR-06 | The barcode scanned during the "scan your card" prompt must match a known customer number; if it does, the transaction is linked to that customer and completed; if not, an error is shown and the customer can retry or cancel |
| BR-07 | After a completed transaction, the view resets to an empty state |

---

## Acceptance Criteria

- [ ] Scanning a known barcode adds the product to the item list
- [ ] Scanning an unknown barcode shows an error and does not add anything
- [ ] Scanning the same product twice increments quantity to 2
- [ ] Running total is correct after adding multiple products
- [ ] "Pay" button is disabled when no items are scanned
- [ ] Pressing "Pay" shows a "Scan your store card" prompt
- [ ] Scanning a card barcode after "Pay" completes the transaction
- [ ] After completion, a confirmation message is shown
- [ ] The view resets to empty state after transaction completion

---

## Tests

> Write UI tests that verify the acceptance criteria above. See `architecture.md` § Testing for conventions.

- [ ] `SelfCheckoutTest` (Browserless test — Vaadin Flow view)
- [ ] Tests cover: add product, unknown barcode error, quantity increment, total calculation, pay flow, reset

---

## UI / Routes

The checkout view is a full-screen, touch-friendly layout optimized for a self-checkout terminal.

- **Left/main area**: Item list showing product name, quantity, unit price, and line total
- **Right/bottom area**: Running total (large font), "Pay" button, "Call Employee" button
- **Barcode input**: A hidden/focused text input that captures scanner keyboard input. The input field auto-focuses so scans are always captured.
- **Payment overlay**: When "Pay" is pressed, an overlay/dialog appears saying "Scan your store card" with a cancel option

| Route | Access | Notes |
|-------|--------|-------|
| `/` | public | Vaadin Flow @Route view — main self-checkout terminal |
