# Specification Folder

> Specs are written first, then used as input for AI-driven implementation and verification.
> They are the single source of truth. Keep them up to date as the project evolves.

> **When in doubt, consult the Vaadin MCP.** Whenever you are even slightly unsure about Vaadin API usage, component behavior, theme variables, styling, or best practices — use the Vaadin MCP server to look it up before guessing. Do not rely on memory for Vaadin specifics.

Status lifecycle: **Draft** → **Approved** → **Implemented**

## File Overview

| File | Purpose | When to Read |
|------|---------|--------------|
| `project-context.md` | Vision, problem, users, scope, risks | First — before anything else |
| `architecture.md` | Technology stack and application structure | After project context is filled in |
| `datamodel/datamodel.md` | Entity definitions and relationships | When adding data-driven features |
| `use-cases/use-case-template.md` | Template for individual feature specs | Copy per feature as `use-case-NNN-short-name.md` |
| `use-cases/use-case-001-submit-ticket.md` | Customer submits a support ticket | UC-001 |
| `use-cases/use-case-002-view-my-tickets.md` | Customer views and tracks their tickets | UC-002 |
| `use-cases/use-case-003-manage-ticket-queue.md` | Agent browses and assigns tickets | UC-003 |
| `use-cases/use-case-004-work-on-ticket.md` | Agent comments on and resolves tickets | UC-004 |
| `use-cases/use-case-005-dashboard.md` | Dashboard with ticket metrics | UC-005 |
| `verification.md` | Visual verification checklists (Playwright MCP) | During and after implementation |

## Workflow

1. **Define context** — Fill in `project-context.md` with problem, vision, scope, and constraints.
2. **Outline architecture** — Fill in `architecture.md` with tech stack and application structure.
3. **Specify features** — Copy `use-cases/use-case-template.md` once per feature.
4. **Implement** — Build each use case, referencing its spec for acceptance criteria.
5. **Verify** — Follow `verification.md` checklists for each implemented use case.
6. **Write Tests** — Write UI tests covering acceptance criteria and business rules. Tests must pass before marking as Implemented.
