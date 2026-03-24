# UC-004: Manage Movies (Admin)

**As an** admin, **I want to** manage the movie catalogue **so that** the cinema's offerings stay up to date.

**Status:** Implemented
**Date:** 2026-03-18

---

## Main Flow

- I navigate to `/admin/movies`
- I see a table listing all movies with columns: title, duration, and number of scheduled shows
- I click "Add Movie" to open a form for creating a new movie
- I fill in title, description, duration, and optionally upload a poster image
- I click "Save" and the movie appears in the list
- I can click on an existing movie row to edit it
- I can delete a movie (only if it has no future shows with sold tickets)

---

## Business Rules

| ID | Rule |
|----|------|
| BR-01 | Title is required and must be unique |
| BR-02 | Duration must be a positive number |
| BR-03 | A movie cannot be deleted if it has future shows with at least one sold ticket |
| BR-04 | Poster upload accepts PNG and JPG, max 2 MB, stored in `posters/` |

---

## Acceptance Criteria

- [ ] Admin can view list of all movies
- [ ] Admin can add a new movie with title, description, duration, poster
- [ ] Admin can edit an existing movie
- [ ] Admin can delete a movie without future sold tickets
- [ ] Deleting a movie with future sold tickets shows an error message
- [ ] Form validation enforces required fields and constraints
- [ ] Route requires ADMIN role authentication

---

## UI / Routes

- Server-side Vaadin Flow view (admin layout)
- Standard CRUD pattern: grid + form side by side on desktop

| Route | Access | Notes |
|-------|--------|-------|
| `/admin` | authenticated (ADMIN) | Admin index with links to all admin views |
| `/admin/movies` | authenticated (ADMIN) | Vaadin Flow view |
