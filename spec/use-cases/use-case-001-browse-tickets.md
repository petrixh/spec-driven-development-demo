# UC-001: Browse Tickets

> Browse available transit tickets with filtering by transit mode.

---

**As a** rider, **I want to** browse available tickets by transit mode **so that** I can quickly find the ticket I need.

**Status:** Implemented
**Date:** 2026-02-19

---

## Main Flow

- I open the app and land on the browse page
- I see a grid of ticket cards showing all available tickets
- Each card displays the ticket name, transit mode, ticket type (single ride or day pass), and price
- I see filter buttons at the top for each transit mode: Bus, Train, Metro, Ferry, and an "All" option
- I tap a filter button (e.g., "Bus") and the grid updates to show only tickets for that mode
- I tap "All" to see all tickets again
- I tap on a ticket card to navigate to the detail page for that ticket

---

## Business Rules

| ID | Rule |
|----|------|
| BR-01 | All transit modes must be represented in the seed data (Bus, Train, Metro, Ferry) |
| BR-02 | Each transit mode has both a single-ride and a day-pass ticket |
| BR-03 | The "All" filter is selected by default on page load |
| BR-04 | Ticket cards are tappable — clicking navigates to the detail page |

---

## Acceptance Criteria

- [x] Browse page loads and displays all seeded tickets as cards in a grid
- [x] Filter buttons for All, Bus, Train, Metro, and Ferry are visible
- [x] Selecting a mode filter shows only tickets for that mode
- [x] Selecting "All" shows all tickets
- [x] Each card shows ticket name, transit mode, ticket type, and price
- [x] Tapping a card navigates to the ticket detail page (UC-002) at /ticket/{id}
- [x] Layout is responsive and usable on mobile screens

---

## UI / Routes

> **Design references:** [`Screen1.png`](../../design/screenshots/mobile/Screen1.png) (mobile), [`Screen1-desktop.png`](../../design/screenshots/desktop/Screen1-desktop.png) (desktop), [`design-system.md`](../../design/design-system.md) §5.1–5.2, §6.1. Shared rules: [`design.md`](../design.md).

### Page Header

- Title: "Transit Tickets" (page title style)
- Subtitle: "Find and purchase your ride" (muted text below title)

### Filter Bar

- Horizontal row of text tabs: **All**, **Bus**, **Train**, **Metro**, **Ferry**
- Underline tab style — active tab has amber text with 2px bottom border in amber
- Inactive tabs are muted text, no underline
- "All" is selected by default
- Container has a bottom border separating it from the grid

### Ticket Grid

Each card displays (top to bottom):
1. Transit mode emoji (🚌, 🚆, 🚇, or ⛴️)
2. Ticket name (e.g., "Bus Single Ride")
3. Badge row: mode badge (colored) + type badge (neutral), separated by a small gap
4. Price in the transit mode's signature color

Card styling:
- 3px left border colored by transit mode
- Hover effect: background shifts to elevated surface color
- Entire card surface is clickable — navigates to detail page

Responsive columns:
- Mobile (< 480px): 1 column
- Tablet (480–767px): 2 columns
- Desktop (≥ 768px): 4 columns

### Desktop

- Content area: max-width 960px, centered
- Desktop nav bar visible with "QUICK TRANSIT" branding

| Route | Access | Notes |
|-------|--------|-------|
| `/` | public | Vaadin @Route — main landing page |
