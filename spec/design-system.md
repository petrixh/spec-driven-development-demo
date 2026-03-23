# Design System

> Theme, component usage, and visual standards. Reference this when building or reviewing UI.

---

## 0. Brand

- **App name:** TickTask
- **Tagline:** Project Management
- **Voice:** Playful, clever, developer-friendly. Sounds like "tic-tac" — a nod to the rhythm of getting things done, clock ticks marking progress, and checkmarks (ticks) completing tasks.
- **Logo concept:** A stylized checkmark (tick) integrated with a clock hand, next to three staggered Gantt-style bars in graduated orange tones on a dark background. The checkmark doubles as a clock hand at the "tick" position, reinforcing the dual meaning.

### Logo Files

| File | Size | Usage |
|------|------|-------|
| [`brand/logo.svg`](brand/logo.svg) | 512x512 | App icon, splash screen, about page |
| [`brand/logo-horizontal.svg`](brand/logo-horizontal.svg) | 360x80 | Header/navbar logo with "Forge" wordmark |
| [`brand/favicon.svg`](brand/favicon.svg) | 32x32 | Browser tab favicon |

### Logo Colors

| Element | Color | Token |
|---------|-------|-------|
| Background | `#1A1A1A` | Surface (dark) |
| Checkmark / clock hand | `#FFB74D` | Orange 300 — bright accent |
| Top bar | `#FB8C00` | Orange 600 — planning/future |
| Middle bar | `#F57C00` | Orange 700 — primary/active |
| Bottom bar | `#E65100` | Orange 900 — completed/momentum |

### Usage Rules

- Always display the logo on a white or `#1A1A1A` background — no colored backgrounds.
- Minimum clear space around the logo: equal to the height of the top bar.
- Do not rotate, stretch, or recolor the logo.
- In dark mode contexts, the horizontal wordmark text should switch to `#FFFFFF`.

---

## 1. Theme

- **Base theme:** Vaadin Aura
- **Custom CSS:** `src/main/resources/META-INF/resources/styles.css`

**Aura and Lumo are two different, incompatible design systems.** This project uses **Aura**. Do not use `--lumo-*` CSS variables — they belong to the Lumo theme and must not be mixed with Aura. Use `--aura-*` variables for Aura-specific properties (typography, shadows) and `--vaadin-*` variables for base properties shared across all themes (spacing, radius, colors).

**Always use Aura theme variables instead of hard-coded values** (e.g., `--aura-font-size-xs` through `--aura-font-size-xl` for font sizes). Do not use hardcoded `px`, `rem`, or `em` values when an Aura variable exists. This ensures consistency with the Vaadin Aura theme and allows global adjustments through theme customization.

---

## 2. Color Palette

**Tone: Orange & Black.** The app uses a warm, high-contrast palette built on orange primary and dark neutral tones.

| Token | Value | Usage |
|-------|-------|-------|
| `--vaadin-primary-color` | `#F57C00` (orange 700) | Primary actions, links, active states, Gantt today marker |
| `--vaadin-primary-text-color` | `#E65100` (orange 900) | Primary text on light backgrounds |
| `--vaadin-primary-color-50pct` | `#FB8C0080` | Selections, focus rings, hover highlights |
| `--vaadin-primary-color-10pct` | `#FB8C001A` | Subtle backgrounds (selected rows, active tabs) |
| `--vaadin-error-color` | `#D32F2F` | Error states, destructive actions, validation |
| `--vaadin-success-color` | `#2E7D32` | Success states, DONE badges, progress complete |
| `--vaadin-warning-color` | `#F9A825` | Warnings, workload threshold highlight |
| Surface (light) | `#FFFFFF` | Cards, dialogs, panels |
| Surface (dark) | `#1A1A1A` | App header, sidebar, dark sections |
| Text primary | `#212121` (gray 900) | Headings, body text |
| Text secondary | `#757575` (gray 600) | Labels, captions, timestamps |
| Border | `#E0E0E0` (gray 300) | Card borders, dividers, grid lines |

### Gantt Chart Colors

| Element | Color | Notes |
|---------|-------|-------|
| TODO bar | `#BDBDBD` (gray 400) | Gray for not-started tasks |
| IN_PROGRESS bar | `#F57C00` (orange 700) | Orange matches primary color |
| DONE bar | `#2E7D32` (green 800) | Green for completed |
| Dependency arrow | `#757575` (gray 600) | Subtle, doesn't compete with bars |
| Today marker | `#E65100` (orange 900) | Vertical line, 2px width |

### Priority & Status Badges

| Badge | Background | Text |
|-------|-----------|------|
| TODO | `#EEEEEE` | `#616161` |
| IN_PROGRESS | `#FFF3E0` | `#E65100` |
| DONE | `#E8F5E9` | `#2E7D32` |
| LOW priority | `#EEEEEE` | `#616161` |
| MEDIUM priority | `#FFF3E0` | `#E65100` |
| HIGH priority | `#FFEBEE` | `#C62828` |

---

## 3. Typography

| Element | Font / Size | Notes |
|---------|-------------|-------|
| Headings | Aura defaults (`--aura-font-size-xl` for h1, `--aura-font-size-l` for h2) | Color: `#212121` |
| Body text | Aura defaults (`--aura-font-size-m`) | Color: `#212121` |
| Captions / labels | `--aura-font-size-s` | Color: `#757575` |

---

## 4. Spacing & Layout

- Vaadin `VerticalLayout` / `HorizontalLayout` with Aura spacing tokens
- Standard padding: `--vaadin-space-m` for card content, `--vaadin-space-l` for page margins
- Max content width: 1280px, centered on desktop
- Dashboard card grid: `--vaadin-space-m` gap between cards

---

## 5. Component Standards

> Preferred Vaadin components and usage patterns.

| Component | When to Use | Notes |
|-----------|-------------|-------|
| `Button` | Primary and secondary actions | Primary buttons use orange (`--vaadin-primary-color`). Destructive buttons use error variant. |
| `Grid` | Task list, team workload table | Enable column sorting. Use row highlighting for workload warnings. |
| `Dialog` | Project creation, delete confirmation | Modal with form or confirmation message. |
| `DatePicker` | Start/end date selection | Used in project and task forms. |
| `Select` / `ComboBox` | Assignee selection, status/priority filters | ComboBox for member lists, Select for enums. |
| `Notification` | Feedback after save/delete | Position: bottom-stretch. Success: green. Error: red. Duration: 3s. |
| `Badge` / `Span` | Status and priority indicators | Color-coded per tables above. |
| `SplitLayout` | Gantt chart (task list + timeline) | Vertical splitter between task names and chart area. |

---

## 6. Responsive Behavior

- **Mobile** (< 640px): Single column, stacked layouts, full-width cards
- **Tablet** (640–1024px): Two-column grid, side-by-side content
- **Desktop** (> 1024px): Multi-column grid, admin grid+form side by side

