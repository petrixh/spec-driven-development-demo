# UC-001: Project Dashboard

---

**As a** project manager or team member, **I want to** see all projects with their status at a glance **so that** I can quickly understand what's active, what's behind, and where to focus.

**Status:** Draft
**Date:** 2026-03-23

---

## Main Flow

- I open the application and land on the Project Dashboard.
- I see a list of all projects as cards, each showing the project name, date range, progress bar, and status badge.
- The progress bar reflects the percentage of tasks marked DONE out of total tasks.
- I can click on a project card to navigate to its Task Management view (UC-002).
- As a project manager, I see a "New Project" button. I click it and a dialog opens where I enter a project name, description, start date, and end date. I save, and the new project appears in the list.
- As a project manager, I can delete a project by clicking a delete icon on the card. A confirmation dialog appears before deletion.

---

## Business Rules

| ID | Rule |
|----|------|
| BR-01 | All fields (name, start date, end date) are mandatory when creating a project. Description is optional. |
| BR-02 | End date must be after start date. |
| BR-03 | Only users with the ADMIN role can create or delete projects. |
| BR-04 | Deleting a project deletes all its tasks and task dependencies. |
| BR-05 | Project status is derived from task statuses and is not manually editable. |

---

## Acceptance Criteria

- [ ] Dashboard shows all projects as cards with name, date range, progress bar, and status badge
- [ ] Progress bar shows percentage of DONE tasks (0% when no tasks exist)
- [ ] Clicking a project card navigates to the Task Management view for that project
- [ ] Admin users see a "New Project" button; non-admin users do not
- [ ] Creating a project with valid data adds it to the dashboard immediately
- [ ] Validation prevents saving a project with missing name or invalid dates
- [ ] Deleting a project requires confirmation and removes it from the dashboard
- [ ] A project with no tasks shows NOT_STARTED status

---

## Tests

> Write UI tests that verify the acceptance criteria above. See `architecture.md` § Testing for conventions.

- [ ] `ProjectDashboardTest` — Browserless test for the dashboard view
- [ ] `ProjectServiceTest` — Service test for CRUD operations and validation

---

## UI / Routes

- Dashboard displays projects as a responsive card grid (single column on mobile, multi-column on desktop).
- Each card shows: project name (heading), date range, horizontal progress bar, status badge (color-coded).
- "New Project" button in the top-right corner (admin only).
- Project creation dialog: form with name (text field), description (text area), start date (date picker), end date (date picker), Save and Cancel buttons.

| Route | Access | Notes |
|-------|--------|-------|
| `/` | authenticated | Vaadin @Route, redirects to dashboard |
| `/projects` | authenticated | Vaadin @Route, Project Dashboard |
