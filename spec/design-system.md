# Design System

> Theme, component usage, and visual standards. Reference this when building or reviewing UI.

---

## 0. Brand

- **App name:** GreenLedger
- **Tagline:** "Expenses, simplified."
- **Logo:** A minimal leaf shape formed from two overlapping rounded rectangles, suggesting both a checkmark (approval) and a leaf (green/growth). Rendered in primary green (`#2E7D32`) on white. Used in the app header and login page.
- **Tone:** Clean, professional, approachable. The brand conveys trust and simplicity — this is a tool that gets out of your way.

---

## 1. Theme

- **Base theme:** Vaadin Aura
- **Custom CSS:** `src/main/resources/META-INF/resources/styles.css`

**Aura and Lumo are two different, incompatible design systems.** This project uses **Aura**. Do not use `--lumo-*` CSS variables — they belong to the Lumo theme and must not be mixed with Aura. Use `--aura-*` variables for Aura-specific properties (typography, shadows) and `--vaadin-*` variables for base properties shared across all themes (spacing, radius, colors).

**Always use Aura theme variables instead of hard-coded values** (e.g., `--aura-font-size-xs` through `--aura-font-size-xl` for font sizes). Do not use hardcoded `px`, `rem`, or `em` values when an Aura variable exists. This ensures consistency with the Vaadin Aura theme and allows global adjustments through theme customization.

---

## 2. Color Palette

Green and white tone theme.

| Token | Value | Usage |
|-------|-------|-------|
| `--primary` | `#2E7D32` | Primary actions, links, active states |
| `--primary-light` | `#4CAF50` | Hover states, secondary highlights |
| `--primary-dark` | `#1B5E20` | Pressed states, emphasis |
| `--background` | `#FFFFFF` | Page and card backgrounds |
| `--surface` | `#F5F9F5` | Subtle green-tinted surface for sections and cards |
| `--text-primary` | `#1C1C1C` | Main body text |
| `--text-secondary` | `#5F6368` | Labels, hints, secondary text |
| `--success` | `#388E3C` | Approved status, success notifications |
| `--error` | `#D32F2F` | Rejected status, validation errors |
| `--warning` | `#F9A825` | Pending status indicators |

---

## 3. Typography

| Element | Font / Size | Notes |
|---------|-------------|-------|
| [Headings] | [e.g., Aura defaults] | [Usage guidance] |
| [Body text] | [e.g., Aura defaults] | [Usage guidance] |

---

## 4. Spacing & Layout

- [Grid or layout system — e.g., Vaadin VerticalLayout / HorizontalLayout defaults]
- [Standard spacing units — e.g., Lumo spacing tokens]
- [Max content width, if any]

---

## 5. Component Standards

> Preferred Vaadin components and usage patterns. List components actually used or planned.

| Component | When to Use | Notes |
|-----------|-------------|-------|
| [e.g., `Button`] | [Primary and secondary actions] | [Use `ButtonVariant.LUMO_PRIMARY` for main CTA] |
| [e.g., `Grid`] | [Tabular data display] | [Always enable column sorting] |
| [e.g., `Notification`] | [User feedback] | [Use appropriate position and duration] |

---

## 6. Responsive Behavior

- **Mobile** (< 640px): Single column, stacked layouts, full-width cards
- **Tablet** (640–1024px): Two-column grid, side-by-side content
- **Desktop** (> 1024px): Multi-column grid, admin grid+form side by side

