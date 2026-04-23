# Quick Transit — Design System

> Visual design specification for the "Warm Amber" dark theme. This document provides everything needed to style the Vaadin Flow application using the Aura theme with custom CSS overrides.

---

## 1. Design Philosophy

**Premium dark transit experience.** The app should feel like a polished, modern mobile tool — warm and inviting rather than cold and techy. The dark background reduces eye strain and makes the amber accent pop, guiding users through the purchase flow with clear visual hierarchy.

**Key principles:**
- Dark surfaces with warm undertones — never blue-tinted blacks
- Amber/gold as the primary accent — used sparingly for prices, totals, active states, and CTAs
- High contrast text on dark backgrounds for readability
- Left-border accent on cards for scannable hierarchy
- Generous spacing — the app should breathe, never feel cramped
- Mobile-first — all touch targets ≥ 44px, comfortable thumb reach

---

## 2. Color Palette

### Core Surfaces

| Token | Hex | Usage |
|-------|-----|-------|
| `--surface-bg` | `#0f0f0f` | Page/app background |
| `--surface-card` | `#1a1a1a` | Cards, panels, dialogs |
| `--surface-input` | `#0f0f0f` | Input field backgrounds |
| `--surface-elevated` | `#222222` | Hover states, elevated surfaces |

### Borders & Dividers

| Token | Hex | Usage |
|-------|-----|-------|
| `--border-default` | `#2a2a2a` | Card borders, input borders, dividers |
| `--border-subtle` | `#1f1f1f` | Row separators inside cards |
| `--border-focus` | `#f59e0b` | Input focus ring (amber) |

### Text

| Token | Hex | Usage |
|-------|-----|-------|
| `--text-primary` | `#fafafa` | Headings, ticket names, primary content |
| `--text-secondary` | `#a3a3a3` | Labels, descriptions, secondary info |
| `--text-muted` | `#737373` | Hints, placeholders, captions |
| `--text-on-accent` | `#000000` | Text on amber buttons/badges |

### Accent — Amber

| Token | Hex | Usage |
|-------|-----|-------|
| `--accent-primary` | `#f59e0b` | Primary buttons, active filters, prices, links |
| `--accent-hover` | `#d97706` | Button hover, link hover |
| `--accent-subtle` | `#f59e0b1a` | Active filter background (amber at 10% opacity) |
| `--accent-border` | `#f59e0b40` | Accent border (amber at 25% opacity) |

### Transit Mode Colors

Each transit mode has a signature color used for card left-borders and mode badges.

| Mode | Hex | Usage |
|------|-----|-------|
| Bus — `--mode-bus` | `#f59e0b` | Amber — matches primary accent |
| Train — `--mode-train` | `#fb923c` | Orange |
| Metro — `--mode-metro` | `#fbbf24` | Gold/Yellow |
| Ferry — `--mode-ferry` | `#a3e635` | Lime green |

Mode colors are used at **full opacity** for left-border accents on cards, and at **10-15% opacity** for badge backgrounds.

### Semantic / Status

| Token | Hex | Usage |
|-------|-----|-------|
| `--success` | `#4ade80` | Confirmation checkmark, success heading |
| `--success-subtle` | `#4ade8020` | Success icon background |
| `--error` | `#f87171` | Validation errors |

---

## 3. Typography

The app uses the system font stack. Vaadin's Aura theme defaults are acceptable — override weight and size as specified below.

### Font Stack
```
font-family: Inter, system-ui, -apple-system, sans-serif;
```

### Type Scale

| Role | Size | Weight | Color | Usage |
|------|------|--------|-------|-------|
| Page Title | 22px | 800 | `--text-primary` | "Transit Tickets", "Checkout" |
| Page Subtitle | 13px | 400 | `--text-muted` | "Find and purchase your ride" |
| Card Title | 14px | 700 | `--text-primary` | Ticket name on browse cards |
| Detail Title | 22px | 800 | `--text-primary` | Ticket name on detail page |
| Section Header | 11px | 600 | `--text-muted` | "ORDER SUMMARY", "PAYMENT DETAILS" — uppercase, letter-spacing: 1px |
| Price (Browse) | 22px | 800 | mode color | Price on ticket cards |
| Price (Large) | 28–32px | 800 | `--accent-primary` | Subtotal, total |
| Price Unit | 14px | 400 | `--text-muted` | "per ticket", "/ ticket" |
| Body Text | 14px | 400 | `--text-secondary` | Descriptions, detail page content |
| Label | 12px | 600 | `--text-muted` | Form labels, field labels |
| Input Text | 14px | 400 | `--text-primary` | Input field values |
| Badge | 11px | 600 | mode color | Transit mode / ticket type tags |
| Confirmation Code | 15px | 700 | `--text-primary` | UUID — use monospace: `'SF Mono', 'Fira Code', 'Courier New', monospace` |
| Detail Row Label | 13px | 400 | `--text-muted` | "Ticket", "Mode", "Quantity" on confirmation |
| Detail Row Value | 13px | 600 | `--text-primary` | Values in confirmation detail rows |

---

## 4. Spacing & Layout

### Base Spacing

| Token | Value | Usage |
|-------|-------|-------|
| `--space-xs` | 4px | Tight gaps (between badge and text) |
| `--space-sm` | 8px | Between badges, small gaps |
| `--space-md` | 12px | Between cards in grid, between form fields |
| `--space-lg` | 16px | Page padding (horizontal), section gaps |
| `--space-xl` | 20px | Major section separation |
| `--space-2xl` | 24px | Card internal padding, top-level content padding |

### Page Layout
- Horizontal page padding: 16px (mobile), 20px (wider content areas)
- Card internal padding: 18–24px
- Maximum content width: 600px (centered on desktop, full-width on mobile)

### Responsive Breakpoints

| Breakpoint | Width | Usage |
|------------|-------|-------|
| Mobile | < 480px | Single-column cards, stacked layouts |
| Tablet | 480–767px | 2-column card grid, stacked detail/checkout |
| Desktop | ≥ 768px | 4-column card grid, side-by-side detail/checkout layouts |

### Content Width
- **Mobile/Tablet:** Full width with 16px horizontal padding
- **Desktop:** `max-width: 960px`, centered with `margin: 0 auto`, 40px horizontal padding
- **Confirmation page (all sizes):** `max-width: 640px`, centered — this page benefits from a narrow, focused layout

### Browse Grid
- **Mobile (< 480px):** 1 column
- **Tablet (480–767px):** 2 columns
- **Desktop (≥ 768px):** 4 columns
- Gap: 12px (mobile/tablet), 16px (desktop)
- Cards have `border-left: 3px solid {mode-color}`

### Detail Page Layout
- **Mobile/Tablet:** Single column — ticket info, then quantity/subtotal, then CTA, all stacked vertically
- **Desktop:** Two-column grid — `1fr 380px`
  - **Left column:** Ticket info card (emoji, name, badges, description, price)
  - **Right column:** Purchase panel card (quantity stepper, subtotal box, CTA button)

### Checkout Page Layout
- **Mobile/Tablet:** Single column — order summary card on top, payment form card below
- **Desktop:** Two-column grid — `1fr 400px`
  - **Left column:** Payment details card (form fields + Purchase button)
  - **Right column:** Order summary card with `position: sticky; top: 24px` — stays visible while scrolling the form
  - Note: on desktop the summary moves to the right sidebar; on mobile it stays above the form

### Confirmation Page Layout
- **All sizes:** Single centered column, `max-width: 640px`
- Desktop gets more vertical padding (48px top vs 20px on mobile)
- UUID code font-size increases to 18px on desktop (15px on mobile)

### Touch Targets
- All buttons: minimum 44px height (48px on desktop)
- Quantity stepper buttons: 48×48px (mobile), 52×52px (desktop)
- Filter tab padding: 10px 14px (mobile), 10px 20px (desktop)
- Card tap area: entire card surface

---

## 5. Component Specifications

### 5.1 Filter Bar (Browse Page)

Style: **Underline tabs** — horizontal row of text buttons with an active underline indicator.

```
Container:
  - border-bottom: 1px solid var(--border-default)
  - padding: 0
  - gap: 0

Filter Button (inactive):
  - padding: 10px 14px
  - background: transparent
  - color: var(--text-muted)
  - font-size: 13px
  - font-weight: 600
  - border: none
  - border-bottom: 2px solid transparent
  - cursor: pointer

Filter Button (active):
  - color: var(--accent-primary)
  - border-bottom: 2px solid var(--accent-primary)
```

Modes: All, Bus, Train, Metro, Ferry. "All" is active by default.

### 5.2 Ticket Card (Browse Page)

```
Card Container:
  - background: var(--surface-card)
  - border-radius: 14px
  - padding: 16px
  - border-top: 1px solid var(--border-default)
  - border-right: 1px solid var(--border-default)
  - border-bottom: 1px solid var(--border-default)
  - border-left: 3px solid {mode-color}
  - cursor: pointer
  - transition: background 0.2s

Card Hover:
  - background: var(--surface-elevated)

Card Content (top to bottom):
  1. Mode emoji icon (18px font-size, 8px margin-bottom)
  2. Ticket name (card-title style)
  3. Badge row: [Mode Badge] [Type Badge] — 6px gap, 10px margin-bottom
  4. Price (22px, weight 800, color: mode-color)
```

### 5.3 Badges

Two types: **Mode badge** and **Type badge**.

```
Mode Badge:
  - font-size: 11px
  - font-weight: 600
  - padding: 2px 8px
  - border-radius: 8px
  - background: {mode-color} at 15% opacity
  - color: {mode-color}
  - border: 1px solid {mode-color} at 25% opacity

Type Badge:
  - Same dimensions as mode badge
  - background: var(--surface-elevated)
  - color: var(--text-secondary)
  - border: 1px solid var(--border-default)
```

### 5.4 Quantity Stepper (Detail Page)

```
Container:
  - display: inline-flex
  - align-items: center
  - background: var(--surface-bg)
  - border-radius: 12px
  - border: 1px solid var(--border-default)

Minus/Plus Buttons:
  - width: 48px, height: 48px
  - background: transparent
  - color: var(--accent-primary)
  - font-size: 20px
  - font-weight: 700
  - border: none
  - cursor: pointer

Value Display:
  - width: 48px
  - text-align: center
  - font-size: 20px
  - font-weight: 800
  - color: var(--text-primary)
```

### 5.5 Primary Button (CTA)

```
  - width: 100% (full-width in flow)
  - padding: 14px 24px
  - border-radius: 12px
  - border: none
  - background: linear-gradient(135deg, #f59e0b, #d97706)
  - color: #000000
  - font-size: 15px
  - font-weight: 700
  - cursor: pointer
  - min-height: 48px

Hover:
  - background: linear-gradient(135deg, #d97706, #b45309)

Disabled:
  - opacity: 0.4
  - cursor: not-allowed
```

### 5.6 Secondary Button (e.g., "Buy Another Ticket")

```
  - width: 100%
  - padding: 14px 24px
  - border-radius: 12px
  - border: 1px solid var(--accent-border)
  - background: transparent
  - color: var(--accent-primary)
  - font-size: 15px
  - font-weight: 700
  - cursor: pointer
```

### 5.7 Text Input Fields (Checkout)

```
  - padding: 11px 14px
  - border-radius: 10px
  - border: 1.5px solid var(--border-default)
  - background: var(--surface-input)
  - color: var(--text-primary)
  - font-size: 14px

Focus:
  - border-color: var(--accent-primary)
  - box-shadow: 0 0 0 2px var(--accent-subtle)

Placeholder:
  - color: var(--text-muted)

Label (above input):
  - font-size: 12px
  - font-weight: 600
  - color: var(--text-muted)
  - margin-bottom: 6px

Validation Error:
  - border-color: var(--error)
  - error message: font-size 12px, color var(--error), margin-top 4px
```

### 5.8 Order Summary Card (Checkout)

```
Container:
  - background: var(--surface-card)
  - border-radius: 14px
  - padding: 18px (mobile), 24px (desktop)
  - border: 1px solid var(--border-default)
  - Desktop: position: sticky, top: 24px (keeps summary visible while scrolling form)

Section Header:
  - "ORDER SUMMARY"
  - font-size: 11px, weight 600, color var(--text-muted)
  - text-transform: uppercase, letter-spacing: 1px
  - margin-bottom: 12px

Mobile Layout:
  - Flex row, space-between
  - Left: ticket name (15px, 700, primary) + subtitle line (13px, muted) + unit price (12px, dim)
  - Right: total price (24px, 800, accent-primary)

Desktop Layout:
  - Ticket row: mode emoji in 48x48 rounded box (mode-color at 10% bg), name + subtitle to the right
  - Divider line
  - Quantity and Unit Price as label/value rows
  - Divider line
  - Total: label left (15px, 600, secondary), value right (28px, 800, accent-primary)
```

### 5.9 Confirmation Code Block

```
Container:
  - border-radius: 14px
  - padding: 22px
  - text-align: center
  - background: linear-gradient(135deg, rgba(245,158,11,0.1), rgba(217,119,6,0.06))
  - border: 1px solid var(--accent-border)

Label:
  - "CONFIRMATION CODE"
  - font-size: 11px, color var(--text-muted)
  - text-transform: uppercase, letter-spacing: 1.5px
  - margin-bottom: 8px

Code:
  - font-family: 'SF Mono', 'Fira Code', 'Courier New', monospace
  - font-size: 15px
  - font-weight: 700
  - color: var(--text-primary)
  - word-break: break-all
```

### 5.10 Confirmation Details List

```
Container:
  - background: var(--surface-card)
  - border-radius: 14px
  - padding: 18px
  - border: 1px solid var(--border-default)

Row:
  - display: flex, justify-content: space-between
  - padding: 10px 0
  - border-bottom: 1px solid var(--border-subtle) (except last row)

Row Label:
  - font-size: 13px, color var(--text-muted)

Row Value:
  - font-size: 13px, weight 600, color var(--text-primary)
```

### 5.11 Success Header (Confirmation Page)

```
Icon Circle:
  - width: 60px, height: 60px
  - border-radius: 50%
  - background: linear-gradient(135deg, #d97706, #f59e0b)
  - color: #000
  - display: flex, align-items: center, justify-content: center
  - font-size: 28px (checkmark)
  - centered above heading

Heading: "Purchase Successful!"
  - font-size: 22px, weight 800, color var(--text-primary)

Subtitle: "Show this code when boarding"
  - font-size: 14px, color var(--text-muted)
```

### 5.12 Back Navigation Link

```
  - background: none
  - border: none
  - color: var(--accent-primary)
  - font-size: 14px
  - font-weight: 500
  - cursor: pointer
  - padding: 16px (part of top navigation area)
  - Format: "← Back" or "← Back to Browse"
```

---

## 6. Screen-by-Screen Layout

> Mobile layouts shown first, desktop variants below each.

### 6.1 Browse (`/`)

**Mobile (1 column):**
```
┌─────────────────────────────┐
│  Transit Tickets        [h1]│
│  Find and purchase...  [sub]│
│  All  Bus  Train  Metro  Fe │
│  ═══                        │
├─────────────────────────────┤
│ ┌─────────────────────────┐ │
│ │▌🚌 Bus Single Ride     │ │
│ │▌[Bus] [Single] · $2.50 │ │
│ └─────────────────────────┘ │
│ ┌─────────────────────────┐ │
│ │▌🚌 Bus Day Pass        │ │
│ │▌[Bus] [Day Pass]· $7.00│ │
│ └─────────────────────────┘ │
│        ... more cards       │
└─────────────────────────────┘
```

**Desktop (4 columns, max-width 960px centered):**
```
┌──────────────────────────────────────────────────────────────┐
│  Transit Tickets                                             │
│  Find and purchase your ride                                 │
│  All    Bus    Train    Metro    Ferry                        │
│  ════                                                        │
├──────────────────────────────────────────────────────────────┤
│ ┌────────────┐ ┌────────────┐ ┌────────────┐ ┌────────────┐ │
│ │▌🚌        │ │▌🚌        │ │▌🚆        │ │▌🚆        │ │
│ │▌Bus SR    │ │▌Bus DP    │ │▌Train SR  │ │▌Train DP  │ │
│ │▌$2.50    │ │▌$7.00    │ │▌$4.50    │ │▌$12.00   │ │
│ └────────────┘ └────────────┘ └────────────┘ └────────────┘ │
│ ┌────────────┐ ┌────────────┐ ┌────────────┐ ┌────────────┐ │
│ │▌🚇        │ │▌🚇        │ │▌⛴️        │ │▌⛴️        │ │
│ │▌Metro SR  │ │▌Metro DP  │ │▌Ferry SR  │ │▌Ferry DP  │ │
│ │▌$3.00    │ │▌$9.00    │ │▌$5.00    │ │▌$14.00   │ │
│ └────────────┘ └────────────┘ └────────────┘ └────────────┘ │
└──────────────────────────────────────────────────────────────┘
```

### 6.2 Detail (`/ticket/{id}`)

**Mobile (stacked):**
```
┌─────────────────────────────┐
│  ← Back                     │
├─────────────────────────────┤
│  ┌───────────────────────┐  │
│  │  🚌                   │  │
│  │  Bus Single Ride [h2] │  │
│  │  [Bus] [Single Ride]  │  │
│  │  One-way trip on...   │  │
│  │  $2.50 per ticket     │  │
│  │  Quantity             │  │
│  │  [ − ]  1  [ + ]     │  │
│  │  ┌─ Subtotal ──────┐ │  │
│  │  │  $2.50           │ │  │
│  │  └─────────────────┘ │  │
│  │  [Continue to Checkout]│  │
│  └───────────────────────┘  │
└─────────────────────────────┘
```

**Desktop (two-column: info 1fr | purchase panel 380px):**
```
┌──────────────────────────────────────────────────────────────┐
│  ← Back to Browse                                            │
├─────────────────────────────────┬────────────────────────────┤
│  ┌───────────────────────────┐  │  ┌──────────────────────┐  │
│  │  🚌                       │  │  │  Quantity             │  │
│  │  Bus Single Ride     [h2] │  │  │  [ − ]  1  [ + ]    │  │
│  │  [Bus] [Single Ride]      │  │  │                      │  │
│  │                           │  │  │  ┌─ Subtotal ──────┐│  │
│  │  One-way trip on any      │  │  │  │  $2.50          ││  │
│  │  city bus route            │  │  │  └────────────────┘│  │
│  │                           │  │  │                      │  │
│  │  $2.50 per ticket         │  │  │ [Continue to Checkout]│  │
│  └───────────────────────────┘  │  └──────────────────────┘  │
└─────────────────────────────────┴────────────────────────────┘
```

### 6.3 Checkout (`/checkout/{ticketId}/{quantity}`)

**Mobile (stacked: summary on top, payment below):**
```
┌─────────────────────────────┐
│  ← Back to Details          │
│  Checkout              [h2] │
├─────────────────────────────┤
│  ┌───────────────────────┐  │
│  │  ORDER SUMMARY        │  │
│  │  Bus Single Ride      │  │
│  │  Bus · Single · Qty:1 │  │
│  │  Unit: $2.50   $2.50  │  │
│  └───────────────────────┘  │
│  ┌───────────────────────┐  │
│  │  PAYMENT DETAILS      │  │
│  │  Cardholder Name      │  │
│  │  [________________]   │  │
│  │  Card Number          │  │
│  │  [________________]   │  │
│  │  Expiration    CVV    │  │
│  │  [________]  [____]   │  │
│  │  [    Purchase     ]  │  │
│  └───────────────────────┘  │
└─────────────────────────────┘
```

**Desktop (two-column: payment form 1fr | sticky summary 400px):**
```
┌──────────────────────────────────────────────────────────────┐
│  ← Back to Details                                           │
│  Checkout                                                    │
├──────────────────────────────────┬───────────────────────────┤
│  ┌────────────────────────────┐  │  ┌─────────────────────┐  │
│  │  PAYMENT DETAILS           │  │  │  ORDER SUMMARY      │  │
│  │                            │  │  │  🚌 Bus Single Ride │  │
│  │  Cardholder Name           │  │  │  Bus · Single Ride  │  │
│  │  [____________________]    │  │  │  ─────────────────  │  │
│  │                            │  │  │  Quantity      1    │  │
│  │  Card Number               │  │  │  Unit Price  $2.50  │  │
│  │  [____________________]    │  │  │  ═════════════════  │  │
│  │                            │  │  │  Total       $2.50  │  │
│  │  Expiration    CVV         │  │  └─────────────────────┘  │
│  │  [__________] [______]     │  │    ↑ sticky: stays        │
│  │                            │  │    visible on scroll      │
│  │  [      Purchase       ]   │  │                           │
│  └────────────────────────────┘  │                           │
└──────────────────────────────────┴───────────────────────────┘
```

### 6.4 Confirmation (`/confirmation/{code}`)

**Desktop and Mobile share the same structure — single centered column.**
Desktop uses `max-width: 640px` with more top padding (48px).

```
┌─────────────────────────────────────────┐
│              (✓ icon)                   │
│         Purchase Successful!            │
│      Show this code when boarding       │
│                                         │
│  ┌───────────────────────────────────┐  │
│  │       CONFIRMATION CODE           │  │
│  │  bdfdcbdd-38f5-4606-94b4-...     │  │
│  └───────────────────────────────────┘  │
│                                         │
│  ┌───────────────────────────────────┐  │
│  │ Ticket         Bus Single Ride    │  │
│  │ ─────────────────────────────── │  │
│  │ Mode           Bus · Single Ride  │  │
│  │ ─────────────────────────────── │  │
│  │ Quantity        1                 │  │
│  │ ─────────────────────────────── │  │
│  │ Total          $2.50             │  │
│  │ ─────────────────────────────── │  │
│  │ Card           **** 1111         │  │
│  │ ─────────────────────────────── │  │
│  │ Purchased      Feb 19, 2026...  │  │
│  └───────────────────────────────────┘  │
│                                         │
│       [ Buy Another Ticket ]            │
└─────────────────────────────────────────┘
```

---

## 7. Vaadin Aura Theme — CSS Implementation

All styling goes in `src/main/resources/META-INF/resources/styles.css`. Vaadin's Aura theme supports CSS custom properties for deep customization.

### 7.1 Global Theme Overrides

```css
/* ===== WARM AMBER DARK THEME ===== */

html {
  /* Surface colors */
  --lumo-base-color: #0f0f0f;
  --lumo-tint-5pct: #1a1a1a;
  --lumo-tint-10pct: #222222;
  --lumo-tint-20pct: #2a2a2a;
  --lumo-tint-30pct: #333333;
  --lumo-tint-40pct: #444444;
  --lumo-tint-50pct: #555555;
  --lumo-shade-5pct: rgba(0, 0, 0, 0.1);
  --lumo-shade-10pct: rgba(0, 0, 0, 0.2);
  --lumo-shade-20pct: rgba(0, 0, 0, 0.3);

  /* Primary color — Amber */
  --lumo-primary-color: #f59e0b;
  --lumo-primary-color-50pct: rgba(245, 158, 11, 0.5);
  --lumo-primary-color-10pct: rgba(245, 158, 11, 0.1);
  --lumo-primary-text-color: #f59e0b;
  --lumo-primary-contrast-color: #000000;

  /* Text colors */
  --lumo-header-text-color: #fafafa;
  --lumo-body-text-color: #a3a3a3;
  --lumo-secondary-text-color: #737373;
  --lumo-tertiary-text-color: #525252;
  --lumo-disabled-text-color: #404040;

  /* Error color */
  --lumo-error-color: #f87171;
  --lumo-error-text-color: #f87171;

  /* Success color */
  --lumo-success-color: #4ade80;
  --lumo-success-text-color: #4ade80;

  /* Border radius */
  --lumo-border-radius-m: 10px;
  --lumo-border-radius-l: 14px;

  /* Spacing */
  --lumo-space-xs: 4px;
  --lumo-space-s: 8px;
  --lumo-space-m: 12px;
  --lumo-space-l: 16px;
  --lumo-space-xl: 24px;

  /* Font */
  --lumo-font-family: Inter, system-ui, -apple-system, sans-serif;
  --lumo-font-size-xxs: 11px;
  --lumo-font-size-xs: 12px;
  --lumo-font-size-s: 13px;
  --lumo-font-size-m: 14px;
  --lumo-font-size-l: 18px;
  --lumo-font-size-xl: 22px;
  --lumo-font-size-xxl: 28px;

  /* App-specific custom properties */
  --transit-accent: #f59e0b;
  --transit-accent-hover: #d97706;
  --transit-surface-bg: #0f0f0f;
  --transit-surface-card: #1a1a1a;
  --transit-surface-elevated: #222222;
  --transit-border: #2a2a2a;
  --transit-border-subtle: #1f1f1f;
  --transit-mode-bus: #f59e0b;
  --transit-mode-train: #fb923c;
  --transit-mode-metro: #fbbf24;
  --transit-mode-ferry: #a3e635;

  background-color: var(--transit-surface-bg);
  color: var(--lumo-header-text-color);
}
```

### 7.2 Vaadin Component Overrides

```css
/* --- Buttons --- */
vaadin-button[theme~="primary"] {
  background: linear-gradient(135deg, #f59e0b, #d97706);
  color: #000000;
  font-weight: 700;
  border-radius: 12px;
  min-height: 48px;
  font-size: 15px;
  --lumo-button-size: 48px;
}

vaadin-button[theme~="primary"]:hover {
  background: linear-gradient(135deg, #d97706, #b45309);
}

vaadin-button[theme~="tertiary"] {
  color: var(--transit-accent);
  font-weight: 500;
}

/* --- Text Fields --- */
vaadin-text-field,
vaadin-integer-field,
vaadin-password-field {
  --vaadin-input-field-background: var(--transit-surface-bg);
  --vaadin-input-field-border-color: var(--transit-border);
  --vaadin-input-field-border-width: 1.5px;
  --vaadin-input-field-border-radius: 10px;
  --vaadin-input-field-height: 44px;
}

vaadin-text-field[focused],
vaadin-integer-field[focused] {
  --vaadin-input-field-border-color: var(--transit-accent);
}

vaadin-text-field::part(label),
vaadin-integer-field::part(label) {
  font-size: 12px;
  font-weight: 600;
  color: #737373;
}

/* --- Integer Field as Quantity Stepper --- */
vaadin-integer-field[class~="quantity-stepper"] {
  width: 144px;
}

vaadin-integer-field[class~="quantity-stepper"]::part(decrease-button),
vaadin-integer-field[class~="quantity-stepper"]::part(increase-button) {
  width: 48px;
  height: 48px;
  color: var(--transit-accent);
}

vaadin-integer-field[class~="quantity-stepper"]::part(input-field) {
  font-size: 20px;
  font-weight: 800;
  text-align: center;
  background: var(--transit-surface-bg);
  border: 1px solid var(--transit-border);
  border-radius: 12px;
}
```

### 7.3 Application CSS Classes

```css
/* --- Layout --- */
.page-container {
  max-width: 600px;
  margin: 0 auto;
  padding: 0 16px;
}

/* --- Browse Page --- */
.browse-header {
  padding: 24px 0 0;
}

.browse-title {
  font-size: 22px;
  font-weight: 800;
  color: #fafafa;
  margin: 0;
}

.browse-subtitle {
  font-size: 13px;
  color: #737373;
  margin: 4px 0 16px;
}

.filter-bar {
  display: flex;
  gap: 0;
  border-bottom: 1px solid #2a2a2a;
  margin-bottom: 16px;
}

.filter-btn {
  padding: 10px 14px;
  background: transparent;
  color: #737373;
  font-size: 13px;
  font-weight: 600;
  border: none;
  border-bottom: 2px solid transparent;
  cursor: pointer;
  transition: all 0.2s;
}

.filter-btn:hover {
  color: #a3a3a3;
}

.filter-btn.active {
  color: #f59e0b;
  border-bottom-color: #f59e0b;
}

.ticket-grid {
  display: grid;
  grid-template-columns: 1fr;
  gap: 12px;
  padding-bottom: 24px;
}

@media (min-width: 480px) {
  .ticket-grid {
    grid-template-columns: 1fr 1fr;
  }
}

@media (min-width: 768px) {
  .page-container {
    max-width: 960px;
    padding: 0 40px;
  }

  .ticket-grid {
    grid-template-columns: repeat(4, 1fr);
    gap: 16px;
  }

  .filter-btn {
    padding: 10px 20px;
    font-size: 14px;
  }

  /* Detail page: info left, purchase panel right */
  .detail-layout {
    display: grid;
    grid-template-columns: 1fr 380px;
    gap: 32px;
  }

  /* Checkout: payment left, summary right */
  .checkout-layout {
    display: grid;
    grid-template-columns: 1fr 400px;
    gap: 32px;
  }

  .checkout-summary-sticky {
    position: sticky;
    top: 24px;
  }

  /* Confirmation: narrow centered */
  .confirmation-container {
    max-width: 640px;
    margin: 0 auto;
    padding-top: 48px;
  }

  .confirmation-code-value {
    font-size: 18px;
  }
}

/* --- Ticket Card --- */
.ticket-card {
  background: #1a1a1a;
  border-radius: 14px;
  padding: 16px;
  border: 1px solid #2a2a2a;
  border-left-width: 3px;
  cursor: pointer;
  transition: background 0.2s;
}

.ticket-card:hover {
  background: #222222;
}

.ticket-card.mode-bus { border-left-color: #f59e0b; }
.ticket-card.mode-train { border-left-color: #fb923c; }
.ticket-card.mode-metro { border-left-color: #fbbf24; }
.ticket-card.mode-ferry { border-left-color: #a3e635; }

.ticket-card-icon {
  font-size: 18px;
  margin-bottom: 8px;
}

.ticket-card-name {
  font-size: 14px;
  font-weight: 700;
  color: #fafafa;
  margin-bottom: 4px;
}

.ticket-card-price {
  font-size: 22px;
  font-weight: 800;
}

.ticket-card-price.mode-bus { color: #f59e0b; }
.ticket-card-price.mode-train { color: #fb923c; }
.ticket-card-price.mode-metro { color: #fbbf24; }
.ticket-card-price.mode-ferry { color: #a3e635; }

/* --- Badges --- */
.badge {
  display: inline-block;
  font-size: 11px;
  font-weight: 600;
  padding: 2px 8px;
  border-radius: 8px;
  margin-right: 6px;
  margin-bottom: 10px;
}

.badge-mode {
  border-width: 1px;
  border-style: solid;
}

.badge-mode.bus {
  background: rgba(245, 158, 11, 0.15);
  color: #f59e0b;
  border-color: rgba(245, 158, 11, 0.25);
}

.badge-mode.train {
  background: rgba(251, 146, 60, 0.15);
  color: #fb923c;
  border-color: rgba(251, 146, 60, 0.25);
}

.badge-mode.metro {
  background: rgba(251, 191, 36, 0.15);
  color: #fbbf24;
  border-color: rgba(251, 191, 36, 0.25);
}

.badge-mode.ferry {
  background: rgba(163, 230, 53, 0.15);
  color: #a3e635;
  border-color: rgba(163, 230, 53, 0.25);
}

.badge-type {
  background: #222222;
  color: #a3a3a3;
  border: 1px solid #2a2a2a;
}

/* --- Detail Page --- */
.detail-card {
  background: #1a1a1a;
  border-radius: 18px;
  padding: 24px;
  border: 1px solid #2a2a2a;
}

.detail-title {
  font-size: 22px;
  font-weight: 800;
  color: #fafafa;
  margin: 0 0 8px;
}

.detail-price {
  font-size: 28px;
  font-weight: 800;
  color: #fafafa;
  margin-bottom: 24px;
}

.detail-price-unit {
  font-size: 14px;
  font-weight: 400;
  color: #525252;
}

.subtotal-box {
  background: #0f0f0f;
  border-radius: 14px;
  padding: 16px;
  border: 1px solid #2a2a2a;
  margin-bottom: 24px;
}

.subtotal-label {
  font-size: 12px;
  color: #737373;
  text-transform: uppercase;
  letter-spacing: 1px;
}

.subtotal-value {
  font-size: 32px;
  font-weight: 800;
  color: #f59e0b;
}

/* --- Section Headers --- */
.section-header {
  font-size: 11px;
  font-weight: 600;
  color: #737373;
  text-transform: uppercase;
  letter-spacing: 1px;
  margin-bottom: 12px;
}

/* --- Checkout --- */
.summary-card,
.payment-card {
  background: #1a1a1a;
  border-radius: 14px;
  padding: 18px;
  border: 1px solid #2a2a2a;
}

.summary-card {
  margin-bottom: 16px;
}

.summary-total {
  font-size: 24px;
  font-weight: 800;
  color: #f59e0b;
}

/* --- Expiration / CVV side-by-side --- */
.payment-row-inline {
  display: flex;
  gap: 12px;
}

.payment-row-inline > * {
  flex: 1;
}

/* --- Confirmation --- */
.success-icon {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  background: linear-gradient(135deg, #d97706, #f59e0b);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28px;
  color: #000;
  margin: 0 auto 12px;
}

.success-heading {
  font-size: 22px;
  font-weight: 800;
  color: #fafafa;
  text-align: center;
  margin: 0 0 4px;
}

.success-subtitle {
  font-size: 14px;
  color: #737373;
  text-align: center;
  margin: 0 0 24px;
}

.confirmation-code-box {
  border-radius: 14px;
  padding: 22px;
  text-align: center;
  background: linear-gradient(135deg, rgba(245,158,11,0.1), rgba(217,119,6,0.06));
  border: 1px solid rgba(245,158,11,0.25);
  margin-bottom: 16px;
}

.confirmation-code-label {
  font-size: 11px;
  color: #737373;
  text-transform: uppercase;
  letter-spacing: 1.5px;
  margin-bottom: 8px;
}

.confirmation-code-value {
  font-family: 'SF Mono', 'Fira Code', 'Courier New', monospace;
  font-size: 15px;
  font-weight: 700;
  color: #fafafa;
  word-break: break-all;
}

.details-list {
  background: #1a1a1a;
  border-radius: 14px;
  padding: 18px;
  border: 1px solid #2a2a2a;
}

.details-row {
  display: flex;
  justify-content: space-between;
  padding: 10px 0;
  border-bottom: 1px solid #1f1f1f;
}

.details-row:last-child {
  border-bottom: none;
}

.details-row-label {
  font-size: 13px;
  color: #737373;
}

.details-row-value {
  font-size: 13px;
  font-weight: 600;
  color: #fafafa;
}

/* --- Back Link --- */
.back-link {
  background: none;
  border: none;
  color: #f59e0b;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  padding: 16px 0;
  display: block;
}

.back-link:hover {
  color: #d97706;
}

/* --- Responsive Layout Containers --- */

/* Detail page: stacks on mobile, side-by-side on desktop */
.detail-layout {
  display: block; /* mobile: stacked */
}

/* Checkout: stacks on mobile, side-by-side on desktop */
.checkout-layout {
  display: block; /* mobile: stacked */
}

/* Confirmation: always centered, narrower than other pages */
.confirmation-container {
  max-width: 640px;
  margin: 0 auto;
  padding-top: 20px;
}

/* Order summary icon box (desktop checkout) */
.summary-icon-box {
  width: 48px;
  height: 48px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 22px;
  flex-shrink: 0;
}

.summary-icon-box.mode-bus { background: rgba(245, 158, 11, 0.1); }
.summary-icon-box.mode-train { background: rgba(251, 146, 60, 0.1); }
.summary-icon-box.mode-metro { background: rgba(251, 191, 36, 0.1); }
.summary-icon-box.mode-ferry { background: rgba(163, 230, 53, 0.1); }
```

---

## 8. Interaction & Micro-Behavior Notes

| Interaction | Behavior |
|-------------|----------|
| Card hover | Background shifts from `#1a1a1a` → `#222222` (0.2s ease) |
| Filter tap | Instant switch — no animation needed; underline appears under active tab |
| Quantity change | Subtotal text updates immediately; no animation |
| Button hover | Gradient darkens (primary) or background gains slight tint (secondary) |
| Input focus | Border transitions to amber, subtle amber glow appears |
| Page navigation | Vaadin handles transitions; no custom page transition animations needed |
| Purchase button | Disabled (opacity 0.4) until all fields pass validation |

---

## 9. Accessibility Notes

- All text meets WCAG AA contrast on dark backgrounds (`#fafafa` on `#0f0f0f` = 18.1:1 ratio)
- Amber accent `#f59e0b` on `#0f0f0f` = 9.5:1 — passes AA for all text sizes
- Focus indicators use amber border + glow — clearly visible on dark surfaces
- Touch targets are minimum 44px as required by mobile accessibility guidelines
- Semantic HTML: use `<h1>`, `<h2>`, `<label>`, `<button>` appropriately in Vaadin component configuration
- Screen reader: Vaadin components include ARIA attributes by default — ensure custom layouts use `aria-label` where needed

---

## 10. Emoji Icons for Transit Modes

Used on browse cards and detail page headers:

| Mode | Emoji | Notes |
|------|-------|-------|
| Bus | 🚌 | |
| Train | 🚆 | |
| Metro | 🚇 | |
| Ferry | ⛴️ | |

These are displayed as plain text (not images). They render natively on all modern mobile browsers.

---

## 11. File Reference

| File | Purpose |
|------|---------|
| `src/main/resources/META-INF/resources/styles.css` | All CSS from sections 7.1–7.3 above |
| Vaadin Java views | Apply CSS class names from section 7.3 using `addClassName()` |
| No external CSS frameworks | All styling is custom CSS + Vaadin Aura theme overrides |
| No icon libraries needed | Emoji icons are used for transit modes |
