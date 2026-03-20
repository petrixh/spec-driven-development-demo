# UC-005: Manage Shows (Admin)

**As an** admin, **I want to** schedule and manage shows **so that** moviegoers can see what's playing and when.

**Status:** Draft
**Date:** 2026-03-18

---

## Main Flow

- I navigate to `/admin/shows`
- I see a table of all shows with columns: movie title, date/time, room, and tickets sold / total capacity
- I click "Add Show" to open a form
- I select a movie from a dropdown, pick a screening room, and set a date and time
- I click "Save" and the show appears in the list
- I can click on an existing show to edit its date/time or room (not the movie)
- I can delete a show (only if no tickets have been sold)

---

## Business Rules

| ID | Rule |
|----|------|
| BR-01 | A show must be scheduled in the future |
| BR-02 | No two shows in the same room can overlap (based on movie duration + 30 min buffer for cleanup) |
| BR-03 | A show cannot be deleted if any tickets have been sold |
| BR-04 | The movie of an existing show cannot be changed |

---

## Acceptance Criteria

- [ ] Admin can view list of all shows with movie, datetime, room, ticket counts
- [ ] Admin can add a new show by selecting movie, room, and datetime
- [ ] Overlapping show in the same room is rejected with a clear error
- [ ] Admin can edit date/time and room of a show (movie field is read-only)
- [ ] Admin can delete a show with zero tickets sold
- [ ] Deleting a show with sold tickets shows an error message
- [ ] Route requires ADMIN role authentication

---

## UI / Routes

- Server-side Vaadin Flow view (admin layout)
- Grid + form pattern, similar to movie management

| Route | Access | Notes |
|-------|--------|-------|
| `/admin` | authenticated (ADMIN) | Admin index with links to all admin views |
| `/admin/shows` | authenticated (ADMIN) | Vaadin Flow view |
