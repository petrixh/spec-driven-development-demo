# UC-003: Gantt Chart

---

**As a** project manager or team member, **I want to** see all tasks on a timeline with dependencies **so that** I can understand the project schedule, identify bottlenecks, and adjust plans visually.

**Status:** Draft
**Date:** 2026-03-23

---

## Main Flow

- I navigate to the Gantt Chart view from the Task Management view (UC-002) for a specific project.
- I see a Gantt chart with tasks listed as rows on the left and horizontal bars on a timeline to the right.
- Each bar represents a task, spanning from its start date to its end date.
- Bars are color-coded by status: TODO (gray), IN_PROGRESS (blue), DONE (green).
- Dependency arrows are drawn from predecessor task bars to successor task bars (finish-to-start).
- I can zoom the timeline to show days, weeks, or months.
- As a project manager, I can drag a task bar horizontally to change its start and end dates (keeping the same duration). The dates update in real time.
- As a project manager, I can drag the right edge of a task bar to extend or shorten its end date.
- I can click on a task bar to open the task edit panel (same as UC-002).
- Today's date is indicated by a vertical red line on the timeline.

---

## Business Rules

| ID | Rule |
|----|------|
| BR-01 | Task bars must stay within the project's date range. Dragging beyond the project boundary snaps back. |
| BR-02 | Only ADMIN users can drag/resize task bars. Team members can view but not modify. |
| BR-03 | Dependencies are displayed as arrows but cannot be created or removed from the Gantt view (managed separately). |
| BR-04 | When a task is dragged, only that task moves — dependent tasks do NOT automatically shift. |
| BR-05 | The minimum task duration when resizing is 1 day. |

---

## Acceptance Criteria

- [ ] Gantt chart renders all tasks for the project as horizontal bars on a timeline
- [ ] Bars span the correct start-to-end date range for each task
- [ ] Bars are color-coded by task status (gray/blue/green)
- [ ] Dependency arrows render correctly between predecessor and successor tasks
- [ ] Timeline can be zoomed to day, week, or month granularity
- [ ] Today marker (vertical red line) is visible on the timeline
- [ ] Admin users can drag task bars to reschedule (dates update in the database)
- [ ] Admin users can resize task bars to change end date
- [ ] Dragging/resizing respects project date boundaries
- [ ] Non-admin users cannot drag or resize bars
- [ ] Clicking a task bar opens the task edit panel

---

## Tests

> Write UI tests that verify the acceptance criteria above. See `architecture.md` § Testing for conventions.

- [ ] `GanttChartTest` — Browserless test for chart rendering, bar positions, and dependency arrows
- [ ] `GanttInteractionTest` — Browserless test for drag/resize interactions (admin vs non-admin)

---

## UI / Routes

- Left panel: list of task names (rows), aligned with chart rows.
- Right panel: scrollable timeline area with task bars, dependency arrows, and today marker.
- Top toolbar: zoom controls (Day / Week / Month toggle buttons), "Back to Tasks" link.
- Task bars show task name inside (or as tooltip if too narrow).
- Dependency arrows: thin lines with arrowheads, drawn from the end of the predecessor bar to the start of the successor bar.

| Route | Access | Notes |
|-------|--------|-------|
| `/projects/{projectId}/gantt` | authenticated | Vaadin @Route, Gantt Chart |
