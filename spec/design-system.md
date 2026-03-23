# Design System

> Theme, component usage, and visual standards. Reference this when building or reviewing UI.

---

## 1. Brand

- **App name:** Stash.log
- **Tagline:** `git stash` for your warehouse.
- **Logo (full):** [`logo.svg`](logo.svg) — wordmark with icon, used in the sidebar header and login page
- **Logo (icon):** [`logo-icon.svg`](logo-icon.svg) — standalone icon, used as favicon and collapsed sidebar
- **Personality:** Professional, clean, utilitarian — this is an internal productivity tool, not a consumer app. Avoid decoration; every element should earn its space.

---

## 2. Theme

- **Base theme:** Vaadin Aura
- **Custom CSS:** `src/main/resources/META-INF/resources/styles.css`

**Aura and Lumo are two different, incompatible design systems.** This project uses **Aura**. Do not use `--lumo-*` CSS variables — they belong to the Lumo theme and must not be mixed with Aura. Use `--aura-*` variables for Aura-specific properties (typography, shadows) and `--vaadin-*` variables for base properties shared across all themes (spacing, radius, colors).

**Always use Aura theme variables instead of hard-coded values** (e.g., `--aura-font-size-xs` through `--aura-font-size-xl` for font sizes). Do not use hardcoded `px`, `rem`, or `em` values when an Aura variable exists. This ensures consistency with the Vaadin Aura theme and allows global adjustments through theme customization.

---

## 3. Color Palette

| Token | Value | Usage |
|-------|-------|-------|
| `--primary` | `#0F766E` (teal-700) | Primary actions, active states, sidebar accent, links |
| `--primary-dark` | `#115E59` (teal-800) | Hover states on primary elements |
| `--primary-light` | `#CCFBF1` (teal-100) | Subtle backgrounds, selected row highlights |
| `--warning` | `#D97706` (amber-600) | Low-stock badges, warning notifications |
| `--warning-light` | `#FEF3C7` (amber-100) | Warning badge backgrounds |
| `--error` | `#DC2626` (red-600) | Out-of-stock badges, error states, destructive actions |
| `--error-light` | `#FEE2E2` (red-100) | Error badge backgrounds |
| `--success` | `#16A34A` (green-600) | In-stock badges, success notifications |
| `--success-light` | `#DCFCE7` (green-100) | Success badge backgrounds |
| `--neutral-50` | `#F9FAFB` | Page background |
| `--neutral-200` | `#E5E7EB` | Borders, dividers |
| `--neutral-500` | `#6B7280` | Secondary text |
| `--neutral-900` | `#111827` | Primary text |

---

## 4. Typography

| Element | Font / Size | Notes |
|---------|-------------|-------|
| Page headings | Aura defaults (`--aura-font-size-xl`) | One H1 per page, describes the current view |
| Section headings | `--aura-font-size-l` | Used for card titles and section dividers |
| Body text | Aura defaults (`--aura-font-size-m`) | Standard content and form labels |
| Small / captions | `--aura-font-size-s` | Timestamps, secondary info, badge labels |

---

## 5. Spacing & Layout

- Use Vaadin `VerticalLayout` / `HorizontalLayout` with Aura spacing tokens
- Standard page padding: `--vaadin-space-l`
- Card gap: `--vaadin-space-m`
- Max content width: none (full-width grids); dashboard cards max 300px each

---

## 6. Component Standards

> Preferred Vaadin components and usage patterns. List components actually used or planned.

| Component | When to Use | Notes |
|-----------|-------------|-------|
| `Grid` | Inventory list, product list, activity tables | Enable column sorting; use lazy loading for large datasets |
| `Button` | All actions | Primary variant for main CTA ("Save", "Receive"), tertiary for secondary actions |
| `ComboBox` | Product selection, category filter | Use with lazy data provider for product lists |
| `TextField` / `IntegerField` | Form inputs | Always set a label; use `setRequiredIndicatorVisible(true)` for mandatory fields |
| `Notification` | User feedback after actions | Success: bottom-stretch, 3s. Error: middle, persistent until closed |
| `ConfirmDialog` | Destructive confirmations (delete) | Always require explicit confirmation for deletions |
| `Badge` (Span with theme) | Stock status indicators | success = In Stock, warning = Low Stock, error = Out of Stock |

---

## 7. Stock Status Badges

Consistent visual language for inventory status across all views:

| Status | Condition | Badge color | Label |
|--------|-----------|-------------|-------|
| In Stock | currentStock > reorderPoint | `success` | "In Stock" |
| Low Stock | 0 < currentStock ≤ reorderPoint | `warning` | "Low Stock" |
| Out of Stock | currentStock = 0 | `error` | "Out of Stock" |

---

## 8. Responsive Behavior

- **Mobile** (< 640px): Single column, stacked layouts, full-width cards
- **Tablet** (640–1024px): Two-column grid, side-by-side content
- **Desktop** (> 1024px): Multi-column grid, admin grid+form side by side
