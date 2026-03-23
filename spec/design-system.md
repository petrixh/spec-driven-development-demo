# Design System

> Theme, component usage, and visual standards. Reference this when building or reviewing UI.

---

## 0. Brand

- **App name:** re:solve
- **Tagline:** Help desk, simplified.
- **Logo:** `icons/logo.svg` — ticket-with-checkmark icon + "re:solve" wordmark
- **Icon:** `icons/icon.svg` — standalone ticket-with-checkmark (for favicon, nav, etc.)
- **Personality:** Geeky-smart and quietly playful. Professional enough for real work, but with a wink — the kind of tool a developer would name. The colon in "re:solve" sets the tone: clever without trying too hard.

---

## 1. Theme

- **Base theme:** Vaadin Aura
- **Custom CSS:** `src/main/resources/META-INF/resources/styles.css`

**Aura and Lumo are two different, incompatible design systems.** This project uses **Aura**. Do not use `--lumo-*` CSS variables — they belong to the Lumo theme and must not be mixed with Aura. Use `--aura-*` variables for Aura-specific properties (typography, shadows) and `--vaadin-*` variables for base properties shared across all themes (spacing, radius, colors).

**Always use Aura theme variables instead of hard-coded values** (e.g., `--aura-font-size-xs` through `--aura-font-size-xl` for font sizes). Do not use hardcoded `px`, `rem`, or `em` values when an Aura variable exists. This ensures consistency with the Vaadin Aura theme and allows global adjustments through theme customization.

---

## 2. Color Palette

The palette is built around a trustworthy blue primary, with semantic colors for ticket statuses.

| Token | Value | Usage |
|-------|-------|-------|
| `--vaadin-primary-color` | `#2563EB` | Primary actions, links, active states |
| `--vaadin-primary-color-50` | `#EFF6FF` | Primary tint backgrounds (selected rows, hover) |
| `--vaadin-success-color` | `#16A34A` | Resolved status, success notifications |
| `--vaadin-warning-color` | `#D97706` | Medium/High priority indicators, warnings |
| `--vaadin-error-color` | `#DC2626` | Critical priority, error states, destructive actions |

### Status Colors

| Status | Color | Token |
|--------|-------|-------|
| Open | `#2563EB` (blue) | Primary — needs attention |
| In Progress | `#D97706` (amber) | Warning — actively being worked |
| Resolved | `#16A34A` (green) | Success — issue resolved |
| Closed | `#64748B` (slate) | Neutral — no action needed |

### Priority Badge Colors

| Priority | Background | Text |
|----------|------------|------|
| Low | `#F1F5F9` | `#475569` |
| Medium | `#FEF3C7` | `#92400E` |
| High | `#FED7AA` | `#9A3412` |
| Critical | `#FEE2E2` | `#991B1B` |

---

## 3. Typography

| Element | Font / Size | Notes |
|---------|-------------|-------|
| Page headings | Aura defaults (`--aura-font-size-xl`) | One per page, describes the current view |
| Section headings | `--aura-font-size-l` | Cards, panel titles |
| Body text | Aura defaults (`--aura-font-size-m`) | General content, form labels |
| Small / meta text | `--aura-font-size-s` | Timestamps, secondary info, help text |
| Badges / tags | `--aura-font-size-xs` | Status badges, priority labels, uppercase |

---

## 4. Spacing & Layout

- Use Vaadin `VerticalLayout` and `HorizontalLayout` as primary layout components
- Use `--vaadin-space-*` tokens for spacing (`xs`, `s`, `m`, `l`, `xl`)
- Admin views: max content width of `1200px`, centered
- Dashboard cards: equal-width grid using `FlexLayout` or CSS grid
- Forms: max width `600px` to keep fields readable

---

## 5. Component Standards

> Preferred Vaadin components and usage patterns.

| Component | When to Use | Notes |
|-----------|-------------|-------|
| `Button` | Primary and secondary actions | Primary variant for main CTA (Submit, Save); tertiary for low-emphasis actions |
| `Grid` | Ticket queue, breakdown tables | Enable column sorting; use row selection for navigation |
| `FormLayout` | Ticket submission, comment forms | Responsive columns; use `setColspan` for full-width fields |
| `Select` | Category, priority, status dropdowns | Use for fixed enum lists |
| `TextArea` | Description, comment input | Set min-height for comfortable editing |
| `TextField` | Title, search, filter inputs | Always set placeholder text |
| `Notification` | User feedback (success, error) | Position: top-center; duration: 3s for success, 5s for errors |
| `Badge` | Status and priority labels | Color-coded per status/priority tables above |
| `Avatar` | Agent/customer identification | In comment lists, ticket assignment |
| `Details` | Collapsible sections | Ticket details on mobile |

---

## 6. Responsive Behavior

- **Mobile** (< 640px): Single column, stacked layouts, full-width cards
- **Tablet** (640–1024px): Two-column grid, side-by-side content
- **Desktop** (> 1024px): Multi-column grid, admin grid+form side by side

