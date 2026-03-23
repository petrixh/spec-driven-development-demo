# UC-002: Register New Patient

**As a** clinic staff member, **I want to** register a new patient **so that** their information is on file for future visits.

**Status:** Draft
**Date:** 2026-03-23

---

## Main Flow

- I click a "New Patient" button on the patient list view.
- A form opens with fields for patient demographics.
- I fill in first name, last name, date of birth, gender, phone, email, and address.
- I click "Save".
- The system validates the input and saves the patient.
- I see a success notification and am redirected to the patient detail view (UC-003).

---

## Business Rules

| ID | Rule |
|----|------|
| BR-01 | First name, last name, and date of birth are required |
| BR-02 | Date of birth must be in the past |
| BR-03 | Email, if provided, must be a valid email format |
| BR-04 | Phone, if provided, must contain only digits, spaces, dashes, or a leading + |
| BR-05 | A warning is shown if a patient with the same first name, last name, and date of birth already exists (duplicate detection) |

---

## Acceptance Criteria

- [ ] Form displays all patient fields (firstName, lastName, dateOfBirth, gender, phone, email, address)
- [ ] Saving with missing required fields shows validation errors
- [ ] A future date of birth is rejected
- [ ] An invalid email format is rejected
- [ ] A successful save shows a notification and redirects to the detail view
- [ ] Duplicate detection warns when a matching patient already exists

---

## Tests

- [ ] `RegisterPatientTest`
- [ ] Verify form renders all fields
- [ ] Verify required field validation (first name, last name, DOB)
- [ ] Verify date of birth must be in the past
- [ ] Verify email format validation
- [ ] Verify successful save creates patient and shows notification
- [ ] Verify duplicate detection warning

---

## UI / Routes

- A form layout with labeled fields, grouped logically (name, contact, address).
- "Save" and "Cancel" buttons at the bottom.

| Route | Access | Notes |
|-------|--------|-------|
| `/patients/new` | authenticated (ADMIN) | Vaadin @Route |
