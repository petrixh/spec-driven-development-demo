# Design System

> Theme, component usage, and visual standards. Reference this when building or reviewing UI.

---

## 1. Theme

- **Base theme:** Vaadin Aura
- **Custom CSS:** `src/main/resources/META-INF/resources/styles.css`

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

- [Breakpoints — e.g., mobile < 640px, tablet < 1024px, desktop >= 1024px]
- [Layout changes at breakpoints]
- [Components that adapt — e.g., Grid columns, navigation]

