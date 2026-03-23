# UC-002: Task Management

---

**As a** project manager, **I want to** create, edit, and organize tasks within a project **so that** the team knows what needs to be done and by when.

**Status:** Draft
**Date:** 2026-03-23

---

## Main Flow

- I navigate to a project (from the dashboard) and land on the Task Management view.
- I see a grid/table of all tasks for this project with columns: name, assignee, start date, end date, priority, and status.
- I click "Add Task" and a side panel or dialog opens with a form. I fill in the task name, description, start date, end date, priority, assignee (select from team members), and save.
- The new task appears in the grid.
- I click on an existing task row to open it for editing. I change the status from TODO to IN_PROGRESS and save.
- I can delete a task via a delete button in the edit form. A confirmation dialog appears before deletion.
- I can sort the grid by any column and filter by status or assignee.
- A "Gantt Chart" button/tab lets me switch to the Gantt view (UC-003) for this project.

---

## Business Rules

| ID | Rule |
|----|------|
| BR-01 | Task name and start/end dates are mandatory. Description, assignee, and priority are optional (priority defaults to MEDIUM). |
| BR-02 | Task start date must not be before the project start date. Task end date must not be after the project end date. |
| BR-03 | Task end date must be on or after task start date. |
| BR-04 | Only ADMIN users can create or delete tasks. All authenticated users can edit task status. |
| BR-05 | Deleting a task removes its dependency links (both as predecessor and successor). |
| BR-06 | A task's assignee must be a member of the project's team. |

---

## Acceptance Criteria

- [ ] Task grid shows all tasks for the selected project with correct columns
- [ ] Admin users see "Add Task" button; non-admin users do not
- [ ] Creating a task with valid data adds it to the grid
- [ ] Validation prevents saving a task with missing required fields or dates outside the project range
- [ ] Clicking a task row opens it for editing
- [ ] Any authenticated user can change task status
- [ ] Deleting a task requires confirmation and removes it from the grid
- [ ] Grid supports sorting by each column
- [ ] Grid supports filtering by status and assignee
- [ ] Navigation to Gantt Chart view is available from this view

---

## Tests

> Write UI tests that verify the acceptance criteria above. See `architecture.md` § Testing for conventions.

- [ ] `TaskManagementTest` — Browserless test for the task grid and CRUD operations
- [ ] `TaskServiceTest` — Service test for task CRUD, validation, and date constraints

---

## UI / Routes

- Top section: project name as heading, "Add Task" button (admin only), "Gantt Chart" toggle/tab.
- Main area: Vaadin Grid with columns for name, assignee, start date, end date, priority (badge), status (badge).
- Task edit: side panel (drawer) with form fields and Save / Delete / Cancel buttons.
- Status badges: TODO (gray), IN_PROGRESS (blue), DONE (green).
- Priority badges: LOW (gray), MEDIUM (yellow), HIGH (red).

| Route | Access | Notes |
|-------|--------|-------|
| `/projects/{projectId}/tasks` | authenticated | Vaadin @Route, Task Management |
