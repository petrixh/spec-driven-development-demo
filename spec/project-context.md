# Project Context

> High-level context for the project: the problem being solved, who it's for, what's in scope, and what constraints apply.

## 1. Vision

A lightweight internal help desk application where employees submit support tickets and agents resolve them. Customers get a simple, self-service portal to create tickets and track their status. Agents get a queue-based workflow to triage, work on, and resolve tickets efficiently. Managers get visibility into team workload and performance through a dashboard.

Success looks like: tickets flow from submission to resolution with minimal friction, nothing falls through the cracks, and the team has clear visibility into what's open and how fast things get resolved.

## 2. Users

| Role | Description | Access |
|------|-------------|--------|
| **Customer** | Any employee who needs support. Submits tickets and tracks their status. | Public views (React/Hilla) |
| **Agent** | Support staff who work tickets. Can view the full queue, assign tickets to themselves, add comments, and change status. | Admin views (Vaadin Flow, `ADMIN` role) |
| **Manager** | Supervises the support team. Same access as Agent, plus a dashboard with team-level metrics. | Admin views (Vaadin Flow, `ADMIN` role) |

## 3. Constraints

- Single-tenant, internal use — no multi-org or public registration
- Authentication via Spring Security; customers use a simple login, agents/managers have the `ADMIN` role
- No email notifications in initial scope — all interaction happens in-app

> For technology stack and application structure details, see [`architecture.md`](architecture.md).

---

# Related Documents

- [Spec README](README.md) — process overview and workflow
- [Architecture](architecture.md) — technology stack and application structure
- [Use Case Template](use-cases/use-case-template.md) — template for feature specifications
- [Verification](verification.md) — visual verification checklists
