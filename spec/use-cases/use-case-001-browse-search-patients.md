# UC-001: Browse & Search Patients

**As a** clinic staff member, **I want to** browse and search the patient list **so that** I can quickly find the patient I need.

**Status:** Draft
**Date:** 2026-03-23

---

## Main Flow

- I log in and land on the patient list view.
- I see a table of patients showing name, date of birth, phone number, and email.
- I type into a search field at the top of the table to filter patients by name or date of birth.
- The table updates as I type, showing only matching patients.
- I click a row to navigate to that patient's detail view (UC-003).

---

## Business Rules

| ID | Rule |
|----|------|
| BR-01 | The patient list is sorted alphabetically by last name by default |
| BR-02 | Search matches against first name, last name, and date of birth |
| BR-03 | The table uses lazy loading / pagination for large datasets |
| BR-04 | An empty search field shows all patients |

---

## Acceptance Criteria

- [ ] Patient list displays columns: last name, first name, date of birth, phone, email
- [ ] Typing in the search field filters the list in real time
- [ ] Clicking a patient row navigates to the patient detail view
- [ ] The list is sorted by last name ascending by default
- [ ] An empty state message is shown when no patients match the search

---

## Tests

- [ ] `BrowseSearchPatientsTest`
- [ ] Verify table displays patient data with correct columns
- [ ] Verify search filters patients by name
- [ ] Verify search filters patients by date of birth
- [ ] Verify empty search shows all patients
- [ ] Verify clicking a row navigates to detail view
- [ ] Verify empty state when no results match

---

## UI / Routes

- Grid/table as the main component, with a text field above it for search.
- Columns: Last Name, First Name, Date of Birth, Phone, Email.

| Route | Access | Notes |
|-------|--------|-------|
| `/patients` | authenticated (ADMIN) | Main landing view, Vaadin @Route |
