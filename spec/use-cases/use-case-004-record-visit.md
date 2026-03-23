# UC-004: Record Patient Visit

**As a** clinic staff member, **I want to** record a visit for a patient **so that** we have a history of their appointments and treatments.

**Status:** Draft
**Date:** 2026-03-23

---

## Main Flow

- I am on the patient detail view (UC-003) and click "Add Visit".
- A form or dialog opens with fields for date, reason, doctor name, and notes.
- I fill in the visit details and click "Save".
- The system validates the input and saves the visit.
- I see a success notification and the visit appears in the patient's visit history list.

---

## Business Rules

| ID | Rule |
|----|------|
| BR-01 | Date and reason are required |
| BR-02 | Visit date defaults to today |
| BR-03 | Visit date cannot be in the future |
| BR-04 | The visit history list is sorted by date descending (most recent first) |

---

## Acceptance Criteria

- [ ] "Add Visit" button is available on the patient detail view
- [ ] Visit form contains fields: date, reason, doctor name, notes
- [ ] Date defaults to today's date
- [ ] Saving with missing required fields shows validation errors
- [ ] A future visit date is rejected
- [ ] A successful save shows a notification and the new visit appears in the list
- [ ] Visits are displayed in reverse chronological order

---

## Tests

- [ ] `RecordVisitTest`
- [ ] Verify "Add Visit" opens a form/dialog
- [ ] Verify date defaults to today
- [ ] Verify required field validation (date, reason)
- [ ] Verify future date is rejected
- [ ] Verify successful save adds visit to history
- [ ] Verify visit list is sorted by date descending

---

## UI / Routes

- Visit form opens as a dialog on the patient detail view (no separate route).
- Fields: Date (date picker), Reason (text field), Doctor Name (text field), Notes (text area).
- "Save" and "Cancel" buttons in the dialog.

| Route | Access | Notes |
|-------|--------|-------|
| (no separate route — dialog on `/patients/{patientId}`) | authenticated (ADMIN) | Dialog opened from patient detail view |
