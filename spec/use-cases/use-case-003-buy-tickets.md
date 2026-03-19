# UC-003: Select Seats & Buy Tickets

**As a** moviegoer, **I want to** choose my seats and purchase tickets **so that** I have reserved seats for the show.

**Status:** Implemented
**Date:** 2026-03-18

---

## Main Flow

- I arrive at the seat selection page for a specific show
- I see the movie title, showtime, and screening room name at the top
- I see a visual seat map showing all seats in the room, laid out in rows
- Available seats are shown in one color, sold seats in another (not clickable)
- I click on available seats to select them (they highlight in a third color)
- I can click a selected seat again to deselect it
- Below the seat map, I see a summary: number of selected seats
- I enter my name and email address
- I click "Purchase" to complete the transaction
- I see a confirmation message with my ticket details

---

## Business Rules

| ID | Rule |
|----|------|
| BR-01 | Maximum 6 tickets per transaction |
| BR-02 | A seat that is already sold cannot be selected |
| BR-03 | Name and email are required to complete purchase |
| BR-04 | Email must be a valid email format |
| BR-05 | If another user buys the same seat between selection and purchase, show an error and refresh the seat map |
| BR-06 | On successful purchase, tickets are created and the confirmation is shown |

---

## Acceptance Criteria

- [ ] Seat map displays all seats with correct rows and columns
- [ ] Sold seats are visually distinct and not selectable
- [ ] Clicking an available seat selects it; clicking again deselects
- [ ] Maximum 6 seats can be selected
- [ ] Name and email fields are required and validated
- [ ] Purchase creates ticket records and shows confirmation
- [ ] Concurrent booking conflict is handled gracefully
- [ ] Page is accessible without authentication

---

## UI / Routes

- Screen indicator at the top of the seat map (a curved bar labeled "Screen")
- Seat map: grid of clickable seat icons, labeled by row letter and seat number
- Legend: Available / Selected / Sold
- Purchase form below the map with name, email, and "Purchase" button
- Confirmation shown inline after purchase (no page navigation)

| Route | Access | Notes |
|-------|--------|-------|
| `/show/{id}` | public | React-based seat selection & purchase |
