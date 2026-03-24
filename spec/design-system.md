# Design System

> Theme, component usage, and visual standards. Reference this when building or reviewing UI.

---

## 1. Theme

- **Base theme:** Vaadin Aura
- **Custom CSS:** `src/main/resources/META-INF/resources/styles.css`

**Aura and Lumo are two different, incompatible design systems.** This project uses **Aura**. Do not use `--lumo-*` CSS variables — they belong to the Lumo theme and must not be mixed with Aura. Use `--aura-*` variables for Aura-specific properties (typography, shadows) and `--vaadin-*` variables for base properties shared across all themes (spacing, radius, colors).

**Always use Aura theme variables instead of hard-coded values** (e.g., `--aura-font-size-xs` through `--aura-font-size-xl` for font sizes). Do not use hardcoded `px`, `rem`, or `em` values when an Aura variable exists. This ensures consistency with the Vaadin Aura theme and allows global adjustments through theme customization.

---

## 2. Color Palette

| Token | Value | Usage |
|-------|-------|-------|
| [e.g., `--primary`] | [e.g., `#1676F3`] | [Primary actions, links] |
| [e.g., `--error`] | [e.g., `#E53935`] | [Error states, destructive actions] |

---

## 3. Typography

| Element | Font / Size | Notes |
|---------|-------------|-------|
| [Headings] | [e.g., Aura defaults] | [Usage guidance] |
| [Body text] | [e.g., Aura defaults] | [Usage guidance] |

---

## 4. Spacing & Layout

- [Grid or layout system — e.g., Vaadin VerticalLayout / HorizontalLayout defaults]
- [Standard spacing units — e.g., `--vaadin-spacing-*` tokens]
- [Max content width, if any]

---

## 5. Component Standards

> Preferred Vaadin components and usage patterns. List components actually used or planned.

| Component | When to Use | Notes |
|-----------|-------------|-------|
| [e.g., `Button`] | [Primary and secondary actions] | [Use primary variant for main CTA] |
| [e.g., `Grid`] | [Tabular data display] | [Always enable column sorting] |
| [e.g., `Notification`] | [User feedback] | [Use appropriate position and duration] |

---

## 6. Responsive Behavior

- **Mobile** (< 640px): Single column, stacked layouts, full-width cards
- **Tablet** (640–1024px): Two-column grid, side-by-side content
- **Desktop** (> 1024px): Multi-column grid, admin grid+form side by side

