# Project Context

> High-level context for the project: the problem being solved, who it's for, what's in scope, and what constraints apply.

## 1. Vision

A lightweight project management tool for small teams (2–10 people) who need to plan work, track progress, and visualize timelines without the complexity of enterprise tools like Jira or MS Project. The app centers on a Gantt chart as the primary planning interface, complemented by straightforward task management and a team workload view. Success means a team lead can set up a project, assign tasks, and see the full timeline in under five minutes.

## 2. Users

- **Project Manager (Admin)** — Creates and manages projects, assigns tasks to team members, adjusts timelines. Has full access to all views and data.
- **Team Member (Authenticated)** — Views assigned tasks, updates task status, views the Gantt chart and workload. Cannot create or delete projects.

## 3. Constraints

- Single-tenant application (one team per deployment)
- No integrations with external systems (email, Slack, etc.)
- No file attachments or comments on tasks
- No recurring tasks or templates
- Maximum 20 projects and 200 tasks total (small-team scale)

> For technology stack and application structure details, see [`architecture.md`](architecture.md).

---

# Related Documents

- [Spec README](README.md) — process overview and workflow
- [Architecture](architecture.md) — technology stack and application structure
- [Use Case Template](use-cases/use-case-template.md) — template for feature specifications
- [Verification](verification.md) — visual verification checklists
