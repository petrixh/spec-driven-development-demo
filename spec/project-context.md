# Project Context

> High-level context for the project: the problem being solved, who it's for, what's in scope, and what constraints apply.

## 1. Vision

A lightweight internal expense management application that lets employees submit expenses and managers review them. The goal is a simple, self-contained tool that covers the core submit-review-report lifecycle without the complexity of a full enterprise expense system.

## 2. Users

- **Employee** — Submits expenses with details and receipts. Can browse and track the status of their own submissions.
- **Manager** — Reviews pending expenses from team members, approves or rejects them, and has a dashboard for spending overview.

## 3. Constraints

- Single-level approval only (no multi-level approval chains)
- No external integrations (no ERP, payroll, or email notifications)
- Receipt upload limited to images (JPEG, PNG)

> For technology stack and application structure details, see [`architecture.md`](architecture.md).

---

# Related Documents

- [Spec README](README.md) — process overview and workflow
- [Architecture](architecture.md) — technology stack and application structure
- [Use Case Template](use-cases/use-case-template.md) — template for feature specifications
- [Verification](verification.md) — visual verification checklists
