# Verification

> Visual verification process using Playwright MCP, plus a per-use-case checklist.
> Each checklist references specific design screenshots for comparison.

---

## 1. Visual Verification Process

Use the Playwright MCP server to visually verify each use case after implementation.

### Steps

1. **Ensure the application is running**
2. **Navigate to the route** — open the page defined in the use case's UI/Routes section
3. **Walk through the main flow** — perform each step from the use case's Main Flow
4. **Take screenshots** — capture the page state at key interaction points
5. **Compare against design screenshots:**
   - Resize browser to **mobile width (~390px)** and compare against the corresponding `design/screenshots/mobile/` screenshot
   - Resize browser to **desktop width (~1080px)** and compare against the corresponding `design/screenshots/desktop/` screenshot
   - Check **structural fidelity** — layout, component placement, visual hierarchy. This is not a pixel-perfect comparison; focus on whether the implementation faithfully represents the design intent
6. **Record results** — note any visual issues in the per-use-case checklist below

---

## 2. Per-Use-Case Verification Checklist

> Copy the template below for new use cases. Populated checklists for UC-001 through UC-004 follow.

### Template: UC-[NNN]: [Feature Title]

**Use case spec:** [`use-case-NNN-name.md`](use-cases/use-case-NNN-name.md)
**Mobile screenshot:** `design/screenshots/mobile/ScreenN.png`
**Desktop screenshot:** `design/screenshots/desktop/ScreenN-desktop.png`
**Verified by:** [Name/Agent]
**Date:** [YYYY-MM-DD]

#### Functional

- [ ] Main flow works end-to-end as described in the spec
- [ ] All business rules are enforced (list BR-IDs)
- [ ] All acceptance criteria pass
- [ ] Error/edge cases handled appropriately

#### Visual

- [ ] Page layout matches design screenshot at mobile width (~390px)
- [ ] Page layout matches design screenshot at desktop width (~1080px)
- [ ] Responsive breakpoints work correctly (mobile < 480px, tablet 480–767px, desktop ≥ 768px)
- [ ] Color theme matches warm amber dark theme (dark surfaces, amber accents)
- [ ] Typography hierarchy is correct (title, subtitle, body, labels)
- [ ] Touch targets are minimum 44px height
- [ ] Interactive elements respond correctly (hover, focus, click)

#### Result

- **Status:** [Pass / Fail / Partial]
- **Notes:** [Any issues found or follow-up items]

---

### UC-001: Browse Tickets

**Use case spec:** [`use-case-001-browse-tickets.md`](use-cases/use-case-001-browse-tickets.md)
**Mobile screenshot:** [`Screen1.png`](../design/screenshots/mobile/Screen1.png)
**Desktop screenshot:** [`Screen1-desktop.png`](../design/screenshots/desktop/Screen1-desktop.png)
**Verified by:** Claude (Playwright MCP)
**Date:** 2026-04-23

#### Functional

- [x] Main flow works end-to-end as described in the spec
- [x] All business rules are enforced: BR-01, BR-02, BR-03, BR-04
- [x] All acceptance criteria pass
- [x] Error/edge cases handled appropriately

#### Visual

- [x] Page layout matches `Screen1.png` at mobile width (~390px)
- [x] Page layout matches `Screen1-desktop.png` at desktop width (~1080px)
- [x] Responsive grid: 1 column (mobile < 480px), 2 columns (tablet 480-767px), 4 columns (desktop >= 768px)
- [x] Filter bar: underline tabs with amber active state and 2px bottom border
- [x] Ticket cards: 3px left border in mode color, emoji, name, badges, price
- [x] Color theme matches warm amber dark theme (dark surfaces, amber accents)
- [x] Typography: page title (22px/800), subtitle (13px/muted), card titles (14px/700), prices (22px/800) in correct hierarchy
- [x] Touch targets: filter tabs and card surfaces are clickable
- [x] Interactive elements: card hover effect, filter tab switching

#### Result

- **Status:** Pass
- **Notes:** All 8 tickets rendered in grid across 4 transit modes (Bus, Train, Metro, Ferry). Filter bar with All/Bus/Train/Metro/Ferry tabs working — All selected by default. Filtering correctly narrows results: Bus shows 2, Metro shows 2, Ferry shows 2. Card click navigates to /ticket/{id}. Responsive breakpoints verified: 1 column at 390px, 2 columns at 600px, 4 columns at 1080px. Dark theme with amber accents applied via custom CSS. Card left borders match transit mode colors (amber=Bus, orange=Train, gold=Metro, lime=Ferry). Badge styling applied with mode-colored mode badges and neutral type badges.

---

### UC-002: Ticket Detail

**Use case spec:** [`use-case-002-ticket-detail.md`](use-cases/use-case-002-ticket-detail.md)
**Mobile screenshot:** [`Screen2.png`](../design/screenshots/mobile/Screen2.png)
**Desktop screenshot:** [`Screen2-desktop.png`](../design/screenshots/desktop/Screen2-desktop.png)
**Verified by:** Claude (Playwright MCP)
**Date:** 2026-03-13

#### Functional

- [x] Main flow works end-to-end as described in the spec
- [x] All business rules are enforced: BR-01, BR-02, BR-03, BR-04
- [x] All acceptance criteria pass
- [x] Error/edge cases handled appropriately

#### Visual

- [x] Page layout matches `Screen2.png` at mobile width (~390px)
- [x] Page layout matches `Screen2-desktop.png` at desktop width (~1080px)
- [x] Responsive layout: single column (mobile/tablet), 2-panel with 380px right panel (desktop)
- [x] Back navigation link visible at top ("← Back" on mobile, "← Back to Browse" on desktop)
- [x] Ticket info: large emoji, heading, badge row, description, price with "per ticket"
- [x] Quantity stepper: 48×48px buttons, centered value display
- [x] Subtotal box: elevated card with "SUBTOTAL" label and large amber price
- [x] Primary button: amber gradient, black text, full-width
- [x] Color theme matches warm amber dark theme
- [x] Touch targets: stepper buttons and CTA meet 44px minimum

#### Result

- **Status:** Pass
- **Notes:** All functional and visual checks pass. Quantity stepper increments/decrements correctly with real-time subtotal updates. Continue to Checkout navigates to /checkout/{ticketId}/{quantity}. Responsive layout switches between single column (mobile) and two-panel (desktop) correctly.

---

### UC-003: Checkout

**Use case spec:** [`use-case-003-checkout.md`](use-cases/use-case-003-checkout.md)
**Mobile screenshot:** [`Screen3.png`](../design/screenshots/mobile/Screen3.png)
**Desktop screenshot:** [`Screen3-desktop.png`](../design/screenshots/desktop/Screen3-desktop.png)
**Verified by:** Claude (Playwright MCP)
**Date:** 2026-03-13

#### Functional

- [x] Main flow works end-to-end as described in the spec
- [x] All business rules are enforced: BR-01, BR-02, BR-03, BR-04, BR-05, BR-06, BR-07
- [x] All acceptance criteria pass
- [x] Error/edge cases handled appropriately

#### Visual

- [x] Page layout matches `Screen3.png` at mobile width (~390px)
- [x] Page layout matches `Screen3-desktop.png` at desktop width (~1080px)
- [x] Responsive layout: summary on top + form below (mobile), form left + sticky summary right at 400px (desktop)
- [x] Column order swap: summary first on mobile, moves to right sidebar on desktop
- [x] Back navigation "← Back to Details" visible at top
- [x] Order summary card: "ORDER SUMMARY" header, ticket name, subtitle, unit price, total in amber
- [x] Payment form card: "PAYMENT DETAILS" header, labeled input fields, expiration + CVV side-by-side
- [x] Purchase button: disabled until form valid (reduced opacity), amber gradient when enabled
- [x] Color theme matches warm amber dark theme
- [x] Touch targets: input fields and purchase button meet 44px minimum

#### Result

- **Status:** Pass
- **Notes:** All functional and visual checks pass. Form validation correctly disables Purchase button until all fields are valid (16-digit card number, MM/YY expiration, 3-digit CVV, non-empty cardholder). Purchase creates PurchaseOrder with UUID and navigates to confirmation page. Desktop summary shows emoji icon box, quantity, unit price, and total rows. Mobile summary shows compact layout with ticket info and total.

---

### UC-004: Confirmation

**Use case spec:** [`use-case-004-confirmation.md`](use-cases/use-case-004-confirmation.md)
**Mobile screenshot:** [`Screen4.png`](../design/screenshots/mobile/Screen4.png)
**Desktop screenshot:** [`Screen4-desktop.png`](../design/screenshots/desktop/Screen4-desktop.png)
**Verified by:** Claude (Playwright MCP)
**Date:** 2026-03-13

#### Functional

- [x] Main flow works end-to-end as described in the spec
- [x] All business rules are enforced: BR-01, BR-02, BR-03, BR-04
- [x] All acceptance criteria pass
- [x] Error/edge cases handled appropriately

#### Visual

- [x] Page layout matches `Screen4.png` at mobile width (~390px)
- [x] Page layout matches `Screen4-desktop.png` at desktop width (~1080px)
- [x] Single centered column at all sizes, max-width 640px
- [x] Success header: amber gradient circle (60px) with checkmark, heading, subtitle — all centered
- [x] Confirmation code block: amber gradient background, "CONFIRMATION CODE" label, UUID in monospace
- [x] Confirmation code font size: 15px mobile, 18px desktop
- [x] Details list: label-value rows with dividers (Ticket, Mode, Quantity, Total, Card, Purchased)
- [x] Secondary button: transparent background, amber border and text
- [x] Color theme matches warm amber dark theme
- [x] Touch targets: secondary button meets 44px minimum

#### Result

- **Status:** Pass
- **Notes:** All functional and visual checks pass. Confirmation page displays UUID code, ticket details, masked card number (**** **** **** XXXX), and formatted purchase timestamp. "Buy Another Ticket" navigates back to browse page. Invalid confirmation codes redirect to browse page. Desktop uses 18px monospace for UUID, mobile uses 15px with word-break.

---

### UC-005: Lookup Purchase

**Use case spec:** [`use-case-005-lookup.md`](use-cases/use-case-005-lookup.md)
**Mobile screenshot:** N/A — follows UC-004 visual pattern (no dedicated mockup)
**Desktop screenshot:** N/A — follows UC-004 visual pattern (no dedicated mockup)
**Verified by:** Claude (Playwright MCP)
**Date:** 2026-03-16

#### Functional

- [x] Lookup page loads with text input and "Look Up" button
- [x] Entering a valid UUID that matches a purchase displays full purchase details
- [x] Ticket name, transit mode, ticket type, quantity, total price, card last 4, and purchase date shown
- [x] Confirmation code is prominently displayed in the result
- [x] Entering a valid UUID with no matching purchase shows "No purchase found for this confirmation code"
- [x] Entering an invalid UUID format shows "Please enter a valid confirmation code"
- [x] Input field retains its value after an error for correction
- [x] "Buy Another Ticket" button navigates to browse page
- [x] "Look Up Another" button clears result and returns to input state
- [x] All business rules enforced: BR-01, BR-02, BR-03, BR-04, BR-05

#### Visual

- [x] Single centered column at all sizes, max-width 640px
- [x] Page heading and subtitle centered
- [x] Input field and button are full-width within the container
- [x] Error message displayed below input in red/error styling
- [x] Result display matches UC-004 confirmation pattern (code box + detail rows)
- [x] Responsive breakpoints work correctly (mobile < 480px, tablet 480–767px, desktop ≥ 768px)
- [x] Color theme matches warm amber dark theme (dark surfaces, amber accents)
- [x] Touch targets are minimum 44px height
- [x] Desktop gets additional top padding (48px vs 20px on mobile)

#### Result

- **Status:** Pass
- **Notes:** All functional and visual checks pass. Lookup page displays input form with UUID text field and "Look Up" button. Invalid UUID format shows "Please enter a valid confirmation code" error. Valid UUID with no match shows "No purchase found for this confirmation code" error. Input is retained on errors. Successful lookup displays purchase details matching UC-004 confirmation pattern (magnifying glass icon, code box, detail rows). "Look Up Another" resets to input. "Buy Another Ticket" navigates to browse. Desktop shows nav bar with "Lookup" active and extra top padding. Follows UC-004 visual pattern — no dedicated design mockups.
