# Project Context

> High-level context for the project: the problem being solved, who it's for, what's in scope, and what constraints apply.

## 1. Vision

A small clinic needs a simple, reliable web application to manage patient information and visit history. Staff should be able to quickly find patients, register new ones, update their details, and log visits — all from a single browser-based tool. The goal is to replace paper records and spreadsheets with a clean, purpose-built application that reduces errors and saves time.

## 2. Users

- **Clinic Staff (ADMIN role)** — Front-desk and medical staff who register patients, update records, and log visits. They have full access to all features behind login.

## 3. Constraints

- All views require authentication (no public views)
- Patient data must be validated before saving (required fields, format checks)
- The application is single-clinic; no multi-tenancy

> For technology stack and application structure details, see [`architecture.md`](architecture.md).

---

# Related Documents

- [Spec README](README.md) — process overview and workflow
- [Architecture](architecture.md) — technology stack and application structure
- [Use Case Template](use-cases/use-case-template.md) — template for feature specifications
- [Verification](verification.md) — visual verification checklists
