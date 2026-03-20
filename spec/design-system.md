# Design System

> Theme, component usage, and visual standards. Reference this when building or reviewing UI.

---

## 1. Theme

- **Base theme:** Vaadin Aura (dark mode for public views, default for admin views)
- **Custom CSS:** `src/main/resources/META-INF/resources/styles.css`

**Aura and Lumo are two different, incompatible design systems.** This project uses **Aura**. Do not use `--lumo-*` CSS variables — they belong to the Lumo theme and must not be mixed with Aura. Use `--aura-*` variables for Aura-specific properties (typography, shadows) and `--vaadin-*` variables for base properties shared across all themes (spacing, radius, colors).

**Always use Aura theme variables instead of hard-coded values** (e.g., `--aura-font-size-xs` through `--aura-font-size-xl` for font sizes). Do not use hardcoded `px`, `rem`, or `em` values when an Aura variable exists. This ensures consistency with the Vaadin Aura theme and allows global adjustments through theme customization.

---

## 2. Color Palette

| Token | Value | Usage |
|-------|-------|-------|
| `--primary` | `#E50914` | Primary actions, accents (cinema red) |
| `--primary-hover` | `#B2070F` | Hover state for primary elements |
| `--surface` | `#141414` | Page background (public views) |
| `--surface-card` | `#1F1F1F` | Card backgrounds (public views) |
| `--text-primary` | `#FFFFFF` | Primary text on dark backgrounds |
| `--text-secondary` | `#B3B3B3` | Secondary text, labels |
| `--seat-available` | `#2E7D32` | Available seat indicator |
| `--seat-selected` | `#1565C0` | Selected seat indicator |
| `--seat-sold` | `#424242` | Sold seat indicator |

---

## 3. Typography

| Element | Font / Size | Notes |
|---------|-------------|-------|
| Headings | Aura defaults, bold | Movie titles use h2 on detail page |
| Body text | Aura defaults | Descriptions, labels |
| Card titles | 1.1rem, semi-bold | Movie card titles |

---

## 4. Spacing & Layout

- Use Vaadin layout components (VerticalLayout, HorizontalLayout) for admin views
- Use standard CSS flexbox/grid for React public views
- Standard spacing: Lumo spacing tokens (S, M, L, XL)
- Max content width: 1200px for public views, full width for admin grid views
- Public pages are centered with padding on larger screens

---

## 5. Component Standards

> Preferred Vaadin components and usage patterns.

| Component | When to Use | Notes |
|-----------|-------------|-------|
| `Grid` | Admin list views (movies, shows) | Enable column sorting, use lazy loading |
| `FormLayout` | Admin edit forms | Two columns on desktop, single on mobile |
| `Button` | Actions | Use `ButtonVariant.LUMO_PRIMARY` for main CTA |
| `TextField` / `TextArea` | Text input | Always set label and placeholder |
| `DateTimePicker` | Show scheduling | For selecting show date and time |
| `ComboBox` | Movie/room selection | For dropdowns with search |
| `Notification` | User feedback | Position TOP_CENTER, 3s duration for success, 5s for errors |
| `Upload` | Poster images | Accept PNG/JPG only, max 2 MB |

---

## 6. Responsive Behavior

- **Mobile** (< 640px): Single column, stacked layouts, full-width cards
- **Tablet** (640–1024px): Two-column movie grid, side-by-side movie info
- **Desktop** (> 1024px): 3–4 column movie grid, admin grid+form side by side
- Seat map scrolls horizontally on small screens if needed

