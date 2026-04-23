# UC-002: Ticket Detail

> View ticket details and select the number of tickets to purchase.

---

**As a** rider, **I want to** see the details of a ticket and choose how many to buy **so that** I can proceed to checkout with the right quantity.

**Status:** Implemented
**Date:** 2026-02-19

---

## Main Flow

- I arrive on the detail page after tapping a ticket card on the browse page
- I see the ticket name, transit mode, ticket type, a description, and the unit price
- I see a quantity selector defaulting to 1
- I adjust the quantity (e.g., increase to 3)
- I see the subtotal update in real time (quantity × unit price)
- I tap a "Continue to Checkout" button to proceed to the summary/payment page

---

## Business Rules

| ID | Rule |
|----|------|
| BR-01 | Quantity must be between 1 and 5 |
| BR-02 | Default quantity is 1 |
| BR-03 | Subtotal is calculated as quantity × unit price and displayed in real time |
| BR-04 | The "Continue to Checkout" button is always enabled (quantity is always valid given the constraints) |

---

## Acceptance Criteria

- [ ] Detail page displays ticket name, transit mode, ticket type, description, and unit price
- [ ] Quantity selector defaults to 1
- [ ] Quantity cannot go below 1 or above 5
- [ ] Subtotal updates in real time when quantity changes
- [ ] "Continue to Checkout" button navigates to the checkout page (UC-003) passing ticket and quantity
- [ ] A back link or button allows returning to the browse page
- [ ] Layout is responsive and usable on mobile screens

---

## UI / Routes

> **Design references:** [`Screen2.png`](../../design/screenshots/mobile/Screen2.png) (mobile), [`Screen2-desktop.png`](../../design/screenshots/desktop/Screen2-desktop.png) (desktop), [`design-system.md`](../../design/design-system.md) §5.4–5.6, §6.2. Shared rules: [`design.md`](../design.md).

### Back Navigation

- "← Back to Browse" link at top of page (amber text, no background)

### Ticket Info

- Large transit mode emoji
- Ticket name as heading (detail title style)
- Badge row: mode badge (colored) + type badge (neutral)
- Description text (body text style)
- Unit price with "per ticket" suffix (price in mode color, suffix muted)

### Quantity Stepper

- "Quantity" label above the stepper
- Custom stepper: minus button, centered value display, plus button
- Stepper buttons are 48×48px with amber-colored minus/plus icons
- Value displays centered between buttons

### Subtotal Box

- Elevated card with subtle border
- "SUBTOTAL" label (uppercase, muted, small text with letter-spacing)
- Large price value in amber

### Call to Action

- "Continue to Checkout" — primary button (amber gradient, black text, full-width)

### Responsive Layout

- **Mobile/Tablet:** Single stacked column — ticket info, quantity stepper, subtotal, and CTA all stack vertically
- **Desktop:** 2-panel layout — ticket info card on the left (1fr), purchase panel (quantity, subtotal, CTA) on the right (380px fixed)

| Route | Access | Notes |
|-------|--------|-------|
| `/ticket/{ticketId}` | public | Vaadin @Route with URL parameter |
