# Specification Folder

Specs are written first, then used as input for AI-driven implementation and verification.
They are the single source of truth. Keep them up to date as the project evolves.

Status: **Pending** (not yet implemented) or **Implemented**

A use case cannot be marked as Implemented unless all criteria in skills/use-case-implementation.md are fulfilled

## File Overview

| File | Purpose | When to Read |
|------|---------|--------------|
| `project-context.md` | Vision, problem, users, scope, risks | First — before anything else |
| `architecture.md` | Technology stack and application structure | After project context is filled in |
| `datamodel/datamodel.md` | Entity definitions and relationships | When adding data-driven features |
| `use-cases/use-case-template.md` | Template for individual feature specs | Copy per feature as `use-case-NNN-short-name.md` |
| `design-system.md` | Theme, component usage, and visual standards | When building or reviewing UI |
| `skills/use-case-implementation.md` | How to implement a use case (steps, iteration) | When implementing a use case |
| `skills/use-case-tests.md` | How to write tests for a use case | When writing tests |
| `skills/visual-verification.md` | How to visually verify an implemented use case | During and after implementation |

## Workflow

### Create Specs

1. **Define context** — Fill in `project-context.md` with problem, vision, scope, and constraints.
2. **Outline architecture** — Fill in `architecture.md` with tech stack and application structure.
3. **Specify features** — Copy `use-cases/use-case-template.md` once per feature.

### Implement Specs

1. Ask the AI to implement a given use case, or update it according to updated specs

If implementation is lacking, add more information to specs files and ask again.

Avoid providing project related information in a prompt to the AI as those details will not be recorded in the project itself.
