# UC-002: View Showtimes & Select Show

**As a** moviegoer, **I want to** see available showtimes for a movie **so that** I can pick a convenient time.

**Status:** Pending
**Date:** 2026-03-18

---

## Main Flow

- I click on a movie from the landing page
- I see the movie detail page with poster, title, description, and duration
- Below the movie info, I see a list of upcoming showtimes grouped by date
- Each showtime shows the time, screening room name, and number of available seats
- I click on a showtime to proceed to seat selection

---

## Business Rules

| ID | Rule |
|----|------|
| BR-01 | Only future showtimes are displayed |
| BR-02 | Showtimes are grouped by date and sorted chronologically |
| BR-03 | Available seat count = total seats in room minus sold tickets for that show |
| BR-04 | Sold-out shows are visible but cannot be selected |

---

## Acceptance Criteria

- [ ] Movie detail page shows poster, title, description, duration
- [ ] Upcoming showtimes listed, grouped by date
- [ ] Each showtime displays time, room, and available seat count
- [ ] Sold-out shows are visually distinct and not clickable
- [ ] Clicking an available showtime navigates to seat selection (`/show/{id}`)
- [ ] Page is accessible without authentication

---

## UI / Routes

- Movie info section at the top (poster + text side by side on desktop, stacked on mobile)
- Showtime list below, organized by date with date headers

| Route | Access | Notes |
|-------|--------|-------|
| `/movie/{id}` | public | React-based movie detail |
