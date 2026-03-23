# Data Model

> Entity definitions and relationships. Evolves as features are added.

| Entity | Key Fields | Relationships |
|--------|-----------|---------------|
| Project | id, name, description, startDate, endDate, status (NOT_STARTED, IN_PROGRESS, COMPLETED) | Has many Tasks, has many Members |
| Task | id, name, description, startDate, endDate, status (TODO, IN_PROGRESS, DONE), priority (LOW, MEDIUM, HIGH) | Belongs to Project, assigned to Member, has many dependency Tasks |
| Member | id, name, email, role (MANAGER, DEVELOPER, DESIGNER, QA) | Has many Tasks, belongs to many Projects |
| TaskDependency | id, predecessorTaskId, successorTaskId | Links two Tasks (finish-to-start) |

## Notes

- Task dates must fall within the parent Project's date range.
- A Task can have zero or more predecessors (finish-to-start dependencies shown on the Gantt chart).
- Members are shared across projects (team roster).
- Project status is derived: NOT_STARTED if all tasks are TODO, COMPLETED if all tasks are DONE, otherwise IN_PROGRESS.
