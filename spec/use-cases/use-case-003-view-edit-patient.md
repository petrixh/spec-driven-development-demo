# UC-003: View & Edit Patient Details

**As a** clinic staff member, **I want to** view and edit a patient's details **so that** I can keep their information accurate and up to date.

**Status:** Draft
**Date:** 2026-03-23

---

## Main Flow

- I click a patient row on the patient list (UC-001) and am navigated to the detail view.
- I see all patient information displayed in a read-only form.
- I click an "Edit" button to switch the form to editable mode.
- I modify one or more fields and click "Save".
- The system validates the input and saves the changes.
- I see a success notification and the form returns to read-only mode.
- Below the patient details, I see a list of the patient's visits (UC-004).

---

## Business Rules

| ID | Rule |
|----|------|
| BR-01 | The same validation rules from UC-002 apply when editing |
| BR-02 | The patient's visit history is displayed below the details, sorted by date descending |
| BR-03 | Navigating to a non-existent patient ID shows a "Patient not found" message |

---

## Acceptance Criteria

- [ ] Patient details are displayed in read-only mode by default
- [ ] Clicking "Edit" makes the form editable
- [ ] Saving valid changes updates the patient and shows a success notification
- [ ] Validation errors are shown for invalid input (same rules as UC-002)
- [ ] Clicking "Cancel" during edit discards changes and returns to read-only mode
- [ ] Visit history is displayed below the patient details
- [ ] A "Patient not found" message is shown for an invalid patient ID

---

## Tests

- [ ] `ViewEditPatientTest`
- [ ] Verify patient details display correctly in read-only mode
- [ ] Verify edit mode enables form fields
- [ ] Verify saving valid changes updates the patient
- [ ] Verify validation errors appear for invalid input
- [ ] Verify cancel discards unsaved changes
- [ ] Verify visit history list is displayed
- [ ] Verify not-found message for invalid patient ID

---

## UI / Routes

- Top section: patient detail form (read-only by default, editable on click).
- Bottom section: visit history grid showing date, reason, doctor, and notes.
- "Edit" / "Save" / "Cancel" buttons for the form.

| Route | Access | Notes |
|-------|--------|-------|
| `/patients/{patientId}` | authenticated (ADMIN) | Vaadin @Route with URL parameter |
