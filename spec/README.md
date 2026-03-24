# Specification Folder

> Specs are written first, then used as input for AI-driven implementation and verification.
> They are the single source of truth. Keep them up to date as the project evolves.

> **When in doubt, consult the Vaadin MCP.** Whenever you are even slightly unsure about Vaadin API usage, component behavior, theme variables, styling, or best practices — use the Vaadin MCP server to look it up before guessing. Do not rely on memory for Vaadin specifics.

Status: **Draft** (not yet implemented) or **Implemented**

> **A use case cannot be marked as Implemented unless all three are complete:**
> 1. Code is written and functional
> 2. Visual validation has been performed (see `verification.md`)
> 3. Required tests exist and pass (`./mvnw test`)

## File Overview

| File | Purpose | When to Read |
|------|---------|--------------|
| `project-context.md` | Vision, problem, users, scope, risks | First — before anything else |
| `architecture.md` | Technology stack and application structure | After project context is filled in |
| `datamodel/datamodel.md` | Entity definitions and relationships | When adding data-driven features |
| `use-cases/use-case-template.md` | Template for individual feature specs | Copy per feature as `use-case-NNN-short-name.md` |
| `verification.md` | Visual verification checklists (Playwright MCP) | During and after implementation |

## Workflow

### Phase 1: Create Specs

1. **Define context** — Fill in `project-context.md` with problem, vision, scope, and constraints.
2. **Outline architecture** — Fill in `architecture.md` with tech stack and application structure.
3. **Specify features** — Copy `use-cases/use-case-template.md` once per feature.

### Phase 2: Implement Specs

For each **Draft** use case:

4. **Implement** — Build the use case, referencing its spec for acceptance criteria.
5. **Verify visually** — Follow `verification.md` checklists. Fix any issues found.
6. **Write tests** — Write UI tests covering acceptance criteria and business rules. Tests must pass (`./mvnw test`).
7. **Mark Implemented** — Only after code, visual validation, and passing tests are all complete.
