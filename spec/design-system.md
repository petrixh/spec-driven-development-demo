# Design System

> Theme, component usage, and visual standards. Reference this when building or reviewing UI.

---

## 1. Theme

- **Base theme:** Vaadin Aura
- **Custom CSS:** `src/main/resources/META-INF/resources/styles.css`

> **Aura and Lumo are two different, incompatible design systems.** This project uses **Aura**. Do not use `--lumo-*` CSS variables — they belong to the Lumo theme and must not be mixed with Aura. Use `--aura-*` variables for Aura-specific properties (typography, shadows) and `--vaadin-*` variables for base properties shared across all themes (spacing, radius, colors).

> **When in doubt, consult the Vaadin MCP.** Whenever you are even slightly unsure about Vaadin API usage, component behavior, theme variables, styling, or best practices — use the Vaadin MCP server to look it up before guessing. Do not rely on memory for Vaadin specifics.

---

## 2. Color Palette

| Token | Value | Usage |
|-------|-------|-------|
| `--primary` | `#2E7D32` (green) | Pay button, success states |
| `--primary-light` | `#4CAF50` | Hover states for primary actions |
| `--secondary` | `#1565C0` (blue) | Employee mode actions |
| `--warning` | `#F57C00` (orange) | Call Employee button, employee mode indicator |
| `--error` | `#D32F2F` (red) | Error messages, delete actions |
| `--surface` | `#FAFAFA` | Background for checkout view |

---

## 3. Typography

**Always use Aura theme variables for font sizes** (e.g., `var(--aura-font-size-s)`, `var(--aura-font-size-m)`, `var(--aura-font-size-l)`, `var(--aura-font-size-xl)`). Do not use hardcoded `px`, `rem`, or `em` values for font sizes. This ensures consistency with the Vaadin Aura theme and allows global font-size adjustments through theme customization.

| Element | Font / Size | Notes |
|---------|-------------|-------|
| Total price | `--aura-font-size-xl`, bold | Must be readable from a distance at the checkout terminal |
| Item list | Aura defaults (`--aura-font-size-m`) | Clear and scannable |
| Headings | Aura defaults | Standard section headings |

---

## 4. Spacing & Layout

- Checkout view: Full-viewport layout, no scrolling needed for typical transactions (up to ~10 items, then scroll the item list)
- Admin view: Standard Vaadin VerticalLayout with Aura spacing
- Touch targets: Minimum 48×48px for all buttons on the checkout view (touch-friendly)

---

## 5. Component Standards

> Preferred Vaadin components and usage patterns. List components actually used or planned.

| Component | When to Use | Notes |
|-----------|-------------|-------|
| `Button` | Pay, Call Employee, Paid by Cash | Use `ButtonVariant.PRIMARY` for Pay, distinct styling for employee actions |
| `Grid` | Product admin list | Sortable columns, inline editing |
| `Dialog` | Payment overlay, employee code input, delete confirmation | Modal dialogs for focused interactions |
| `TextField` | Barcode input, product form fields | Hidden barcode input on checkout auto-focuses |
| `Notification` | Scan feedback, errors, confirmation | Brief duration, appropriate positioning |
| `NumberField` | Price input in admin | Ensures numeric input |

---

## 6. Responsive Behavior

- **Mobile** (< 640px): Single column, stacked layouts, full-width cards
- **Tablet** (640–1024px): Two-column grid, side-by-side content
- **Desktop** (> 1024px): Multi-column grid, admin grid+form side by side

