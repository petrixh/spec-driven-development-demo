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

> Calm blue tone — professional and clinical, easy on the eyes for all-day use.

| Token | Value | Usage |
|-------|-------|-------|
| `--vaadin-primary-color` | `#4A90B8` | Primary actions, links, active states |
| `--vaadin-primary-color-50` | `#EBF3F8` | Primary tint — selected row backgrounds, subtle highlights |
| `--vaadin-primary-color-text` | `#3A7CA5` | Primary text — links, emphasized labels |
| `--vaadin-success-color` | `#5BA88C` | Success notifications, save confirmations |
| `--vaadin-error-color` | `#C75B5B` | Error states, validation messages, destructive actions |
| `--vaadin-warning-color` | `#D4A84B` | Warnings, duplicate detection alerts |
| Background | `#F7F9FB` | Page background — very light blue-gray |
| Surface | `#FFFFFF` | Cards, forms, dialogs |
| Text primary | `#2C3E50` | Headings, body text |
| Text secondary | `#6B7D8D` | Labels, helper text, placeholders |
| Border | `#D6E0E8` | Dividers, input borders, table lines |

---

## 3. Typography

| Element | Font / Size | Notes |
|---------|-------------|-------|
| Page titles | Aura default, `--aura-font-size-xl` | One per view, text color `#2C3E50` |
| Section headings | Aura default, `--aura-font-size-l` | Card/section titles |
| Body text | Aura default, `--aura-font-size-m` | General content |
| Helper text | Aura default, `--aura-font-size-s` | Form hints, secondary info, color `#6B7D8D` |

---

## 4. Spacing & Layout

- Use Vaadin `VerticalLayout` and `HorizontalLayout` with Aura/base spacing tokens
- Padding: `--vaadin-padding-xs` (4px), `--vaadin-padding-s` (8px), `--vaadin-padding-m` (12px), `--vaadin-padding-l` (16px)
- Gap: `--vaadin-gap-xs` (4px), `--vaadin-gap-s` (8px), `--vaadin-gap-m` (12px)
- Border radius: `--vaadin-radius-s`, `--vaadin-radius-m`, `--vaadin-radius-l`
- Max content width: 1200px, centered
- Cards and form sections use `--vaadin-padding-m` internal padding

---

## 5. Component Standards

> Preferred Vaadin components and usage patterns. List components actually used or planned.

| Component | When to Use | Notes |
|-----------|-------------|-------|
| `Button` | Primary and secondary actions | Primary variant for main CTA (Save, Add), tertiary for Cancel |
| `Grid` | Patient list, visit history | Enable column sorting; use lazy loading for patient list |
| `FormLayout` | Patient registration and edit forms | Responsive columns (2 on desktop, 1 on mobile) |
| `TextField` | Text input fields | Use helper text for format guidance |
| `DatePicker` | Date of birth, visit date | |
| `ComboBox` | Gender selection | |
| `TextArea` | Visit notes, address | |
| `Dialog` | Visit recording form | Modal dialog from patient detail view |
| `Notification` | Save/error feedback | Position: bottom-stretch, 3s duration for success, persistent for errors |

---

## 6. Responsive Behavior

- **Mobile** (< 640px): Single column, stacked layouts, full-width cards
- **Tablet** (640–1024px): Two-column grid, side-by-side content
- **Desktop** (> 1024px): Multi-column grid, admin grid+form side by side

