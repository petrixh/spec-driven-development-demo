# UC-005: Receipt Printing

---

**As an** employee, **I want to** connect a Bluetooth thermal printer and print receipts **so that** customers receive a printed receipt after their purchase.

**Status:** Draft
**Date:** 2026-03-20

---

## Main Flow

- In employee mode (see UC-002), I press the "Connect Printer" button
- The browser initiates a Web Bluetooth pairing flow
- I select the thermal printer from the Bluetooth device list
- Once connected, a printer status indicator shows the printer is ready
- When any transaction is finalized (card or cash), the receipt is automatically printed on the thermal printer
- The receipt contains: item list with quantities and prices, total, payment method, and date/time
- If no printer is connected when a transaction is finalized, a notification is shown: "No receipt can be printed."

---

## Business Rules

| ID | Rule |
|----|------|
| BR-01 | "Connect Printer" uses the Web Bluetooth API to pair with a Bluetooth thermal printer |
| BR-02 | "Connect Printer" is only available in employee mode |
| BR-03 | The printer connection persists across transactions until the page is reloaded or the printer is disconnected |
| BR-04 | When a transaction is finalized and a printer is connected, a receipt is sent to the thermal printer (item list, quantities, prices, total, payment method, date/time) |
| BR-05 | When a transaction is finalized and no printer is connected, a notification is shown: "No receipt can be printed." |

---

## Acceptance Criteria

- [ ] "Connect Printer" button is visible only in employee mode
- [ ] Pressing "Connect Printer" initiates Bluetooth pairing
- [ ] Printer status indicator shows when a printer is connected
- [ ] Finalizing a transaction with a connected printer prints a receipt
- [ ] Finalizing a transaction without a printer shows "No receipt can be printed." notification
- [ ] Printer connection persists across multiple transactions

---

## Tests

> This feature requires manual testing — Web Bluetooth is not available in browserless/headless environments.

- [ ] Manual: connect printer, finalize transaction, verify receipt prints
- [ ] Manual: finalize transaction without printer, verify notification shown
- [ ] Manual: verify printer stays connected across multiple transactions

---

## UI / Routes

Part of the self-checkout view, visible only in employee mode. No separate route.

- **"Connect Printer" button**: Visible in employee mode, initiates Web Bluetooth pairing
- **Printer status indicator**: Small icon/badge showing connected/disconnected state

| Route | Access | Notes |
|-------|--------|-------|
| `/` | public | Same Vaadin Flow checkout view — printer controls appear in employee mode |
