# UC-001: Browse Movies

**As a** moviegoer, **I want to** see what movies are currently showing **so that** I can decide what to watch.

**Status:** Implemented
**Date:** 2026-03-20

---

## Main Flow

- I open the cinema website at `/`
- I see a grid of movie cards, each showing the poster image, title, and duration
- I can see at a glance which movies have upcoming shows
- I click on a movie card to go to its detail page

---

## Business Rules

| ID | Rule |
|----|------|
| BR-01 | Only movies with at least one future show are displayed |
| BR-02 | Movies are sorted alphabetically by title |
| BR-03 | Poster images served via `/api/posters/` must use `Cache-Control: no-cache` with `ETag` so browsers cache but revalidate every request (304 if unchanged) |

---

## Acceptance Criteria

- [ ] Landing page shows movie cards in a responsive grid
- [ ] Each card displays poster, title, and duration
- [ ] Only movies with future shows appear
- [ ] Clicking a card navigates to the movie detail page (`/movie/{id}`)
- [ ] Page is accessible without authentication

---

## UI / Routes

- Responsive grid: 1 column on mobile, 2 on tablet, 3–4 on desktop
- Movie cards use poster image as the main visual element
- Clean, cinema-themed dark layout

| Route | Access | Notes |
|-------|--------|-------|
| `/` | public | React-based landing page |
