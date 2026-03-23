# Project Context

> High-level context for the project: the problem being solved, who it's for, what's in scope, and what constraints apply.

## 1. Vision

A lightweight, internal Inventory Management application that lets a small warehouse team track products, monitor stock levels, receive inbound shipments, and correct discrepancies — all from a single web interface. The system provides real-time visibility into inventory health through a dashboard with low-stock alerts and recent activity, eliminating spreadsheet-based tracking and reducing stock-out incidents.

## 2. Users

| Role | Description | Permissions |
|------|-------------|-------------|
| **Warehouse Manager** | Oversees inventory operations, maintains product catalog, resolves stock discrepancies | Full access — manage products, receive stock, adjust stock, view dashboard |
| **Warehouse Staff** | Day-to-day receiving and monitoring | Browse inventory, receive stock, view dashboard |

Both roles authenticate via the login page. Role assignment is managed outside the application (e.g., pre-seeded or admin config).

## 3. Constraints

- Internal-only application — no public-facing views required
- Single-warehouse deployment (no multi-location support)
- All monetary values in a single currency (no currency conversion)

> For technology stack and application structure details, see [`architecture.md`](architecture.md).

---

# Related Documents

- [Spec README](README.md) — process overview and workflow
- [Architecture](architecture.md) — technology stack and application structure
- [Use Case Template](use-cases/use-case-template.md) — template for feature specifications
- [Verification](verification.md) — visual verification checklists
