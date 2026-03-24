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

Use Aura theme defaults. Key semantic colors:

| Token | Usage |
|-------|-------|
| `--vaadin-primary-color` | Primary actions (Look up button) |
| `--vaadin-success-color` | Success states (Return OK button) |
| `--vaadin-warning-color` | Warning states (Late return, overdue badges) |
| `--vaadin-error-color` | Error/destructive states (Damaged return button, overdue alerts) |

---

## 3. Typography

Use Aura theme defaults (`--aura-font-size-*` tokens). No custom fonts.

| Element | Token | Notes |
|---------|-------|-------|
| Page headings | `--aura-font-size-xl` | Book title, section headers |
| Labels & captions | `--aura-font-size-s` | Field labels like "PATRON", "ACCOUNT BALANCE" |
| Body text | Aura default | Author, ISBN, status descriptions |
| Monospace values | Monospace font | ISBN, call numbers, patron IDs |

---

## 4. Spacing & Layout

- VerticalLayout as primary container; HorizontalLayout for inline groupings (patron info cards, action buttons)
- Use `--vaadin-space-*` tokens for consistent spacing
- Landing view: centered content, max-width ~500px for the scan input area
- Detail view: max-width ~700px, card-style sections with consistent padding

---

## 5. Component Standards

> Preferred Vaadin components and usage patterns. List components actually used or planned.

| Component | When to Use | Notes |
|-----------|-------------|-------|
| `TextField` | Barcode/ISBN input | Placeholder with example ISBN, auto-focus on landing view |
| `Button` | Actions (Look up, Return OK, Late return, Damaged, Cancel) | Use theme variants for color: primary, success, warning, error |
| `Select` | Damage assessment dropdown | Options: No damage, Minor damage, Major damage |
| `Badge` | Status indicators (Overdue, On time) | Color-coded via theme variants |
| `Notification` | Feedback after processing a return | Success/error variants, appropriate duration |
| `VerticalLayout` / `HorizontalLayout` | Page structure and card groupings | Card-style sections with border and padding |

---

## 6. Responsive Behavior

- **Mobile** (< 640px): Single column, stacked layouts, full-width cards
- **Tablet** (640–1024px): Two-column grid, side-by-side content
- **Desktop** (> 1024px): Multi-column grid, admin grid+form side by side

