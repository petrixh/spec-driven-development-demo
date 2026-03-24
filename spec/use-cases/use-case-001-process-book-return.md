# UC-001: Process Book Return

> Scan a returned book's barcode and process the return.

---

**As a** librarian, **I want to** scan a returned book's barcode and see its details **so that** I can process the return quickly and accurately.

**Status:** Pending
**Date:** 2026-03-24

---

## Main Flow

- I open the application and see the landing view with a barcode input field
- I scan the book's barcode (or type the ISBN manually) and click "Look up"
- The system looks up the book and displays the return processing view:
  - Book details: title, author, ISBN, call number
  - Patron info: name, patron ID, account balance
  - Lending status: checkout date, due date, overdue indicator if applicable, late fee
- I review the information and click "Return OK" to process the return
- The system marks the lending record as returned and shows a success notification
- The view returns to the landing scan screen, ready for the next book

---

## Business Rules

| ID | Rule |
|----|------|
| BR-01 | A valid ISBN/barcode must match an existing book with an active (CHECKED_OUT) lending record |
| BR-02 | If the book is overdue, the number of days overdue and late fee ($0.25/day) are displayed |
| BR-03 | If no matching book or active lending record is found, an error message is shown |
| BR-04 | After a successful return, the view resets to the landing scan screen |

---

## Acceptance Criteria

- [ ] Landing view shows a text field for barcode/ISBN input and a "Look up" button
- [ ] Entering a valid ISBN and clicking "Look up" navigates to the return processing view
- [ ] Return processing view displays book details (title, author, ISBN, call number)
- [ ] Return processing view displays patron info (name, patron ID, account balance)
- [ ] Return processing view displays lending info (checkout date, due date)
- [ ] Overdue books show an "Overdue" badge, days overdue count, and calculated late fee
- [ ] Clicking "Return OK" marks the lending record as RETURNED and shows a success notification
- [ ] After return, the view resets to the landing scan screen
- [ ] Entering an invalid ISBN shows an error message
- [ ] The barcode input field is auto-focused on the landing view

---

## Tests

> Write UI tests that verify the acceptance criteria above. See `architecture.md` § Testing for conventions.

- [ ] `BookReturnViewTest` — Browserless test for the Vaadin Flow view
  - Landing view renders with input field and look up button
  - Valid ISBN lookup shows book details, patron info, and lending info
  - Invalid ISBN shows error message
  - Overdue book displays overdue badge and late fee
  - Clicking "Return OK" processes the return and resets the view
- [ ] `BookReturnServiceTest` — JUnit service test
  - Looking up a book by ISBN returns correct book and active lending record
  - Processing a return updates lending record status
  - Late fee calculation is correct for overdue books
  - Looking up a nonexistent ISBN throws appropriate exception

---

## UI / Routes

Two states within a single Vaadin Flow view:

**Landing state** (see `library-concept/Landing-view-mockup.png`):
- Centered layout with barcode icon, heading "Scan or enter barcode", helper text
- TextField with ISBN placeholder, "Look up" button
- Status text: "Ready — waiting for input"

**Return processing state** (see `library-concept/detail-view-mockup.png`):
- Header: "Return processing" with status badge (Overdue / On time), Cancel button
- Book details card: cover placeholder, title, author, ISBN (monospace), call number (monospace)
- Patron info: name, patron ID, account balance (in side-by-side cards)
- Lending info: overdue alert banner with dates and late fee (if applicable)
- "Return OK" button (green/success variant)
- Rental history summary line

| Route | Access | Notes |
|-------|--------|-------|
| `/` | public (`@AnonymousAllowed`) | Vaadin @Route — single view with landing and processing states |
