# UC-002: Browse Inventory

**As a** Warehouse Staff member, **I want to** view current stock levels in a searchable grid **so that** I can quickly find items and identify low-stock situations.

**Status:** Draft
**Date:** 2026-03-23

---

## Main Flow

- I navigate to the Inventory page.
- I see a grid showing all products with their current stock levels.
- I type a search term in the filter field — the grid filters by product name or SKU.
- I can also filter by category using a dropdown.
- Products at or below their reorder point are visually highlighted (e.g., a warning badge or row styling).
- I can sort by any column by clicking the column header.

---

## Business Rules

| ID | Rule |
|----|------|
| BR-01 | The grid shows all products regardless of stock level (including zero-stock items) |
| BR-02 | A product is considered "low stock" when its current stock is at or below its reorder point |
| BR-03 | Low-stock products are visually distinguished (e.g., warning icon or highlighted row) |
| BR-04 | Search filters by product name or SKU (case-insensitive, partial match) |
| BR-05 | Any authenticated user (ADMIN or staff) can access this view |

---

## Acceptance Criteria

- [ ] The inventory grid displays all products with columns: Name, SKU, Category, Unit Price, Current Stock, Status
- [ ] Typing in the search field filters the grid by name or SKU
- [ ] Selecting a category filters the grid to only that category
- [ ] Low-stock products are visually highlighted
- [ ] Zero-stock products appear with an "Out of stock" indicator
- [ ] Grid columns are sortable
- [ ] Both ADMIN and non-admin authenticated users can access the page

---

## Tests

> Write UI tests that verify the acceptance criteria above. See `architecture.md` § Testing for conventions.

- [ ] `BrowseInventoryTest` — browserless tests covering grid display, filtering, low-stock highlighting, and access control

---

## UI / Routes

- Full-width grid with a toolbar above it containing: a text search field, a category filter dropdown.
- Grid columns: Name, SKU, Category, Unit Price, Current Stock, Status (badge: In Stock / Low Stock / Out of Stock).
- Low-stock rows have a warning-colored status badge. Out-of-stock rows have an error-colored badge.

| Route | Access | Notes |
|-------|--------|-------|
| `/inventory` | authenticated | Vaadin Flow @Route, read-only grid |
