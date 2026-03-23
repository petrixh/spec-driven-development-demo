# Brand Guide — Triage

> Visual identity for the Triage patient management application.

---

## 1. Name

**Triage** — prioritize, assess, act. A term shared by developers and healthcare professionals alike.

- Always written with a capital T: **Triage**
- In the logo, "Tri" is dark (`#2C3E50`) and "age" is blue (`#4A90B8`) — a subtle nod to splitting/parsing

---

## 2. Logo

The logo combines a **shield** with a **medical cross** and a small **heart**, representing protection, healthcare, and compassion.

| Asset | File | Usage |
|-------|------|-------|
| Full logo (icon + wordmark) | [`logo.svg`](logo.svg) | Navigation bar, login page, about page |
| Icon only | [`icon.svg`](icon.svg) | Favicon, app icon, compact spaces |

### Clear space

Maintain padding equal to at least half the icon height around all sides of the logo.

### Minimum size

- Full logo: 160px wide
- Icon only: 24px wide

---

## 3. Brand Colors

Drawn from the [design system](../design-system.md) color palette.

| Color | Hex | Role |
|-------|-----|------|
| Primary Blue | `#4A90B8` | Logo accent, primary actions, active states |
| Deep Blue | `#3A7CA5` | Gradient end, link text |
| Charcoal | `#2C3E50` | Logo "Care" text, headings, body text |
| Light Blue-Gray | `#F7F9FB` | Page backgrounds |
| White | `#FFFFFF` | Surfaces, logo cross/heart |

---

## 4. Typography

- **Logo / App name:** System sans-serif (system-ui), semibold (600)
- **Application UI:** Aura theme defaults (see [design system](../design-system.md))

---

## 5. Tone of Voice

- **Professional** — clear, concise, no jargon
- **Calm** — reassuring, not alarming (even in error states)
- **Helpful** — guide the user, don't lecture
- **Subtly geeky** — small clever touches that developers will appreciate, never forced

Examples:
- Success: "Patient saved successfully"
- Error: "Please fill in the required fields"
- Empty state: "No patients found — try adjusting your search"
