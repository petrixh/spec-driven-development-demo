# UC-005: Lookup Purchase

> Look up an existing purchase by confirmation code and display ticket details.

---

**As a** rider, **I want to** look up my purchase by entering my confirmation code **so that** I can view my ticket details anytime after purchase.

**Status:** Implemented
**Date:** 2026-03-16

---

## Main Flow

- I navigate to the lookup page
- I see a text input field prompting me to enter my confirmation code (UUID)
- I paste or type my confirmation code and tap "Look Up"
- The system validates the UUID format and searches for the purchase
- I see the full purchase details: ticket name, transit mode, ticket type, quantity, total price, last 4 card digits, and purchase date/time
- I see the confirmation code displayed prominently
- I can tap "Buy Another Ticket" to return to the browse page

### Not-Found Flow

- If I enter a UUID that does not match any purchase, I see an error message: "No purchase found for this confirmation code"
- The input field remains populated so I can correct and retry
- If I enter text that is not a valid UUID format, I see an error message: "Please enter a valid confirmation code"

---

## Business Rules

| ID | Rule |
|----|------|
| BR-01 | The confirmation code input must be validated as a valid UUID format before querying the database |
| BR-02 | If no PurchaseOrder exists for the given UUID, an inline error message is shown — no redirect |
| BR-03 | If an invalid (non-UUID) string is entered, a format validation error is shown — no redirect |
| BR-04 | Purchase details displayed match UC-004: ticket name, mode, type, quantity, total, card last 4, purchase date |
| BR-05 | The lookup page is publicly accessible without requiring a prior purchase flow |

---

## Acceptance Criteria

- [ ] Lookup page displays a text input and "Look Up" button
- [ ] Entering a valid UUID that matches a purchase displays full purchase details
- [ ] Ticket name, transit mode, ticket type, quantity, and total price are shown
- [ ] Last 4 digits of the credit card are displayed
- [ ] Purchase date and time are displayed
- [ ] Confirmation code is prominently displayed in the result
- [ ] Entering a valid UUID with no matching purchase shows "No purchase found for this confirmation code"
- [ ] Entering an invalid UUID format shows "Please enter a valid confirmation code"
- [ ] Input field retains its value after an error so the user can correct and retry
- [ ] "Buy Another Ticket" button navigates back to the browse page (UC-001)
- [ ] Layout is responsive and usable on mobile screens

---

## UI / Routes

### Lookup Input

- Centered heading: "Look Up Purchase" (page title style)
- Subtitle: "Enter your confirmation code to view ticket details" (muted text)
- Full-width text input with placeholder "e.g. 550e8400-e29b-41d4-a716-446655440000"
- "Look Up" primary button (amber background, full-width) below the input
- Button is disabled when input is empty

### Error State

- Error message displayed below the input field in red/error styling
- Input field retains its value for correction

### Result Display

- On successful lookup, the result replaces the input area (or appears below it)
- Reuses the same detail display pattern as UC-004 Confirmation:
  - Confirmation code box (amber gradient background, monospace UUID)
  - Details list card with label-value rows: Ticket, Mode, Quantity, Total, Card, Purchased
- "Buy Another Ticket" secondary button (amber border, full-width)
- "Look Up Another" secondary button to clear and search again

### Responsive Layout

- **All sizes:** Single centered column, max-width 640px
- Desktop gets additional top padding (48px vs 20px on mobile)

| Route | Access | Notes |
|-------|--------|-------|
| `/lookup` | public | Vaadin @Route — accepts no URL parameter; UUID is entered via form input |
