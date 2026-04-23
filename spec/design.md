# Design Specification

> Shared visual rules, responsive breakpoints, and component descriptions referenced by all use cases.
> For full CSS values and implementation details, see [`../design/design-system.md`](../design/design-system.md).

---

## 1. Theme Summary

Warm amber dark theme built on Vaadin's Aura base. Dark surfaces with warm undertones, amber/gold as the primary accent for prices, totals, active states, and CTAs. High contrast text on dark backgrounds.

Key design principles:
- Dark background reduces eye strain; amber accent provides visual hierarchy
- Left-border accent on cards for scannable structure
- Generous spacing — the app should breathe, never feel cramped
- Mobile-first — all touch targets minimum 44px

> Full color palette, typography scale, and CSS tokens: [`../design/design-system.md`](../design/design-system.md) §2–3.

---

## 2. Responsive Breakpoints

| Breakpoint | Width | Grid Columns | Layout | Content Width | Padding |
|------------|-------|--------------|--------|---------------|---------|
| Mobile | < 480px | 1 | Stacked | Full width | 16px |
| Tablet | 480–767px | 2 | Stacked | Full width | 16px |
| Desktop | ≥ 768px | 4 | 2-panel (where applicable) | max-width 960px, centered | 40px |
| Confirmation (all sizes) | — | 1 | Single centered column | max-width 640px | — |

> Full spacing tokens and layout rules: [`../design/design-system.md`](../design/design-system.md) §4.

---

## 3. Shared Components

### Transit Mode Colors & Emoji

| Mode | Emoji | Signature Color | Usage |
|------|-------|----------------|-------|
| Bus | 🚌 | Amber | Card left-border, mode badge, price text |
| Train | 🚆 | Orange | Card left-border, mode badge, price text |
| Metro | 🚇 | Gold/Yellow | Card left-border, mode badge, price text |
| Ferry | ⛴️ | Lime green | Card left-border, mode badge, price text |

### Badges

- **Mode badge:** Colored text and border matching the transit mode, translucent mode-color background
- **Type badge:** Neutral — muted text on elevated surface with subtle border

### Primary Button (CTA)

Full-width, amber gradient background, black text, 48px minimum height, 12px border-radius. Disabled state reduces opacity. Used for "Continue to Checkout" and "Purchase".

### Secondary Button

Full-width, transparent background, amber border and text. Used for "Buy Another Ticket".

### Back Navigation

Text link with left arrow (← Back to Browse), amber-colored, no background or border.

### Desktop Nav Bar

"QUICK TRANSIT" branding with page links. Visible only at desktop breakpoint (≥ 768px), hidden on mobile/tablet.

> Full component CSS specifications: [`../design/design-system.md`](../design/design-system.md) §5.

---

## 4. Touch Targets

- All buttons: minimum 44px height (48px on desktop)
- Quantity stepper buttons: 48×48px (mobile), 52×52px (desktop)
- Filter tab padding: 10px 14px (mobile), 10px 20px (desktop)
- Card tap area: entire card surface is clickable

---

## 5. Design Assets

| Asset | Path | Contents |
|-------|------|----------|
| Design system | [`../design/design-system.md`](../design/design-system.md) | Full color palette, typography, spacing, component CSS, layout diagrams |
| Browse — mobile | [`../design/screenshots/mobile/Screen1.png`](../design/screenshots/mobile/Screen1.png) | Browse page at mobile width |
| Browse — desktop | [`../design/screenshots/desktop/Screen1-desktop.png`](../design/screenshots/desktop/Screen1-desktop.png) | Browse page at desktop width |
| Detail — mobile | [`../design/screenshots/mobile/Screen2.png`](../design/screenshots/mobile/Screen2.png) | Ticket detail page at mobile width |
| Detail — desktop | [`../design/screenshots/desktop/Screen2-desktop.png`](../design/screenshots/desktop/Screen2-desktop.png) | Ticket detail page at desktop width |
| Checkout — mobile | [`../design/screenshots/mobile/Screen3.png`](../design/screenshots/mobile/Screen3.png) | Checkout page at mobile width |
| Checkout — desktop | [`../design/screenshots/desktop/Screen3-desktop.png`](../design/screenshots/desktop/Screen3-desktop.png) | Checkout page at desktop width |
| Confirmation — mobile | [`../design/screenshots/mobile/Screen4.png`](../design/screenshots/mobile/Screen4.png) | Confirmation page at mobile width |
| Confirmation — desktop | [`../design/screenshots/desktop/Screen4-desktop.png`](../design/screenshots/desktop/Screen4-desktop.png) | Confirmation page at desktop width |
