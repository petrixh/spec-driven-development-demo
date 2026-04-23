# UC-004: Confirmation

> Display purchase confirmation with ticket details and a unique validation code.

---

**As a** rider, **I want to** see my ticket confirmation with a unique code **so that** I have proof of my purchase.

**Status:** Implemented
**Date:** 2026-02-19

---

## Main Flow

- I arrive on the confirmation page after a successful purchase
- I see a confirmation message (e.g., "Purchase Successful!")
- I see the full ticket details: ticket name, transit mode, ticket type, quantity, and total price
- I see the last 4 digits of the card used
- I see the purchase date and time
- I see a prominent UUID confirmation code that serves as my ticket validation/uniqueness identifier
- I can tap a "Buy Another Ticket" button to return to the browse page

---

## Business Rules

| ID | Rule |
|----|------|
| BR-01 | The UUID confirmation code is generated at purchase time and stored in the PurchaseOrder |
| BR-02 | The confirmation code is displayed prominently as the primary identifier |
| BR-03 | Only the last 4 digits of the credit card are shown |
| BR-04 | The confirmation page is accessible only via a valid purchase — navigating directly without a valid order shows an error or redirects to browse |

---

## Acceptance Criteria

- [ ] Confirmation page displays a success message
- [ ] Ticket name, transit mode, ticket type, quantity, and total price are shown
- [ ] Last 4 digits of the credit card are displayed
- [ ] Purchase date and time are displayed
- [ ] UUID confirmation code is prominently displayed
- [ ] "Buy Another Ticket" button navigates back to the browse page (UC-001)
- [ ] Navigating directly to the confirmation page without a valid purchase redirects to browse
- [ ] Layout is responsive and usable on mobile screens

---

## UI / Routes

> **Design references:** [`Screen4.png`](../../design/screenshots/mobile/Screen4.png) (mobile), [`Screen4-desktop.png`](../../design/screenshots/desktop/Screen4-desktop.png) (desktop), [`design-system.md`](../../design/design-system.md) §5.9–5.11, §6.4. Shared rules: [`design.md`](../design.md).

### Success Header

- Amber gradient circle (60px) with checkmark icon — centered
- Heading: "Purchase Successful!" (page title style, centered)
- Subtitle: "Show this code when boarding" (muted text, centered)

### Confirmation Code Block

- Amber gradient background card with amber border
- Label: "CONFIRMATION CODE" (uppercase, muted, small text with letter-spacing)
- UUID value in monospace font (15px mobile, 18px desktop), bold, full width with word-break

### Details List Card

- Label-value rows with subtle dividers between rows (no divider after last row):
  - Ticket (e.g., "Bus Single Ride")
  - Mode (e.g., "Bus · Single Ride")
  - Quantity
  - Total (price)
  - Card (last 4 digits, e.g., "**** 1111")
  - Purchased (date/time)

### Call to Action

- "Buy Another Ticket" — secondary button (transparent background, amber border and text, full-width)

### Responsive Layout

- **All sizes:** Single centered column, max-width 640px
- Desktop gets additional top padding (48px vs 20px on mobile)
- Confirmation code font size increases on desktop (18px vs 15px)

| Route | Access | Notes |
|-------|--------|-------|
| `/confirmation/{confirmationCode}` | public | Vaadin @Route with UUID parameter — looks up PurchaseOrder by confirmation code |
