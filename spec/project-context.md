# Project Context

> High-level context for the project: the problem being solved, who it's for, what's in scope, and what constraints apply.

## 1. Vision

BuyBuy is a simple self-checkout point-of-sale application for small grocery stores. Customers scan product barcodes using a barcode scanner (connected as a keyboard input device), review their items, and pay using a store-specific loyalty/payment card. The goal is a fast, minimal-friction checkout experience that reduces queues while keeping employee assistance one button-press away.

## 2. Users

- **Customer** — Uses the self-checkout terminal to scan products, review a running total, and pay with their store card. Can call an employee for help.
- **Employee** — Assists customers at the self-checkout. Can unlock employee mode by entering a secret code (hard-coded: `112233`) to access additional controls such as marking a transaction as paid by cash.
- **Admin** — Manages the product catalog (add, edit, delete products) through a password-protected admin interface.

## 3. Constraints

- Barcode scanner input is treated as keyboard input (no special driver/API needed)
- Product data can be auto-fetched from the Open Food Facts API (`https://world.openfoodfacts.org/`); if no match is found, nothing is added automatically — the admin enters details manually
- Store card payment is simulated (scan card barcode to complete payment — no real payment integration)
- The secret employee code is hard-coded as `112233`

> For technology stack and application structure details, see [`architecture.md`](architecture.md).

---

# Related Documents

- [Spec README](README.md) — process overview and workflow
- [Architecture](architecture.md) — technology stack and application structure
- [Use Case Template](use-cases/use-case-template.md) — template for feature specifications
- [Verification](verification.md) — visual verification checklists
