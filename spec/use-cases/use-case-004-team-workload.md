# UC-004: Team Workload

---

**As a** project manager, **I want to** see each team member's task assignments across all projects **so that** I can balance workload and avoid overloading anyone.

**Status:** Draft
**Date:** 2026-03-23

---

## Main Flow

- I navigate to the Team Workload view from the main navigation.
- I see a table with one row per team member. Each row shows the member's name, role, number of active tasks (TODO + IN_PROGRESS), and number of completed tasks (DONE).
- I can expand a row to see the list of tasks assigned to that member, grouped by project, showing task name, project name, dates, and status.
- Rows are highlighted if a member has more than 5 active tasks (workload warning).
- I can click on a task in the expanded row to navigate to the Task Management view (UC-002) for that project.

---

## Business Rules

| ID | Rule |
|----|------|
| BR-01 | The workload view aggregates tasks from all projects, not just one. |
| BR-02 | Active tasks = tasks with status TODO or IN_PROGRESS. |
| BR-03 | Workload warning threshold is 5 active tasks. Rows exceeding this are visually highlighted. |
| BR-04 | Members with zero tasks are still shown (to identify idle team members). |

---

## Acceptance Criteria

- [ ] Table shows all team members with name, role, active task count, and completed task count
- [ ] Expanding a row shows the member's tasks grouped by project
- [ ] Each task shows name, project name, date range, and status
- [ ] Rows with more than 5 active tasks are visually highlighted (e.g., yellow background)
- [ ] Clicking a task in the expanded view navigates to the correct project's Task Management view
- [ ] Members with no assigned tasks show zero counts and are still listed

---

## Tests

> Write UI tests that verify the acceptance criteria above. See `architecture.md` § Testing for conventions.

- [ ] `TeamWorkloadTest` — Browserless test for the workload table, expansion, and highlighting
- [ ] `TeamWorkloadServiceTest` — Service test for workload aggregation across projects

---

## UI / Routes

- Main area: Vaadin Grid with tree/detail functionality for expandable rows.
- Columns: Member Name, Role, Active Tasks (count), Completed Tasks (count).
- Expanded detail: nested list of tasks grouped by project name.
- Warning highlight: yellow-tinted row background for members exceeding the active task threshold.
- Top of page: heading "Team Workload".

| Route | Access | Notes |
|-------|--------|-------|
| `/team` | authenticated | Vaadin @Route, Team Workload |
