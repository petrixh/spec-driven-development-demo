# UC-005: Dashboard

**As a** clinic staff member, **I want to** see a summary dashboard **so that** I can get a quick overview of clinic activity.

**Status:** Draft
**Date:** 2026-03-23

---

## Main Flow

- I log in and navigate to the dashboard (or it is accessible from the navigation).
- I see summary statistics: total number of patients, number of visits today, and number of patients registered this month.
- I see a list of the 10 most recent visits with patient name, date, and reason.
- I see a list of the 5 most recently registered patients with name and registration date.
- I can click on a patient name in either list to navigate to their detail view (UC-003).

---

## Business Rules

| ID | Rule |
|----|------|
| BR-01 | Statistics are computed from current data, not cached |
| BR-02 | "Visits today" counts visits where date equals today |
| BR-03 | "Patients this month" counts patients created in the current calendar month |
| BR-04 | Recent lists are clickable and navigate to the patient detail view |

---

## Acceptance Criteria

- [ ] Dashboard displays total patients count
- [ ] Dashboard displays visits-today count
- [ ] Dashboard displays patients-registered-this-month count
- [ ] A list of the 10 most recent visits is shown with patient name, date, and reason
- [ ] A list of the 5 most recently registered patients is shown with name and date
- [ ] Clicking a patient name navigates to the patient detail view
- [ ] Dashboard handles zero-data state gracefully (no errors when no patients or visits exist)

---

## Tests

- [ ] `DashboardTest`
- [ ] Verify statistics are displayed correctly
- [ ] Verify recent visits list shows up to 10 entries
- [ ] Verify recent patients list shows up to 5 entries
- [ ] Verify clicking a patient navigates to detail view
- [ ] Verify dashboard works with empty data (no patients, no visits)

---

## UI / Routes

- Top row: stat cards (total patients, visits today, new patients this month).
- Below: two-column layout — recent visits (left), recent patients (right).

| Route | Access | Notes |
|-------|--------|-------|
| `/dashboard` | authenticated (ADMIN) | Vaadin @Route |
