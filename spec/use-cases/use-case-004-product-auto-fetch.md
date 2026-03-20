# UC-004: Auto-Fetch Product Data from Open Food Facts

---

**As an** admin, **I want** product data to be automatically fetched from Open Food Facts when adding a new product by barcode **so that** I don't have to manually enter product names.

**Status:** Draft
**Date:** 2026-03-20

---

## Main Flow

- In the product admin view, I enter a barcode in the "add product" form
- When I leave the barcode field (or press a "Lookup" button), the system queries the Open Food Facts API
- If a match is found, the product name is pre-filled from the API response
- I can review and adjust the pre-filled name, then set the price and save
- If no match is found, nothing is pre-filled — I enter the name and price manually
- The lookup is a convenience feature — the admin can always override or skip it

---

## Business Rules

| ID | Rule |
|----|------|
| BR-01 | The API endpoint is `https://world.openfoodfacts.org/api/v2/product/{barcode}` |
| BR-02 | The product name is taken from the `product_name` field in the API response |
| BR-03 | If the API returns no result (status 0) or the request fails, no data is pre-filled and no error is shown — the admin simply enters data manually |
| BR-04 | The API lookup does not create the product — the admin must still confirm and save |
| BR-05 | Price is never fetched from the API (it must always be set by the admin) |

---

## Acceptance Criteria

- [ ] Entering a barcode that exists in Open Food Facts pre-fills the product name
- [ ] Entering a barcode that does not exist in Open Food Facts leaves the name field empty
- [ ] The admin can override the pre-filled name
- [ ] API failure does not block the admin from adding a product manually
- [ ] Price is never pre-filled from the API

---

## Tests

> Write UI tests that verify the acceptance criteria above. See `architecture.md` § Testing for conventions.

- [ ] `ProductAutoFetchTest` (JUnit service test with mocked HTTP client)
- [ ] Tests cover: successful lookup, not-found lookup, API failure handling

---

## UI / Routes

This feature enhances the existing product admin form (UC-003). No new routes.

- **Barcode field**: On blur or via a "Lookup" button, triggers the API lookup
- **Loading indicator**: Brief spinner while the API request is in flight
- **Name field**: Auto-populated if a match is found, editable regardless

| Route | Access | Notes |
|-------|--------|-------|
| `/admin/products` | ADMIN role | Same as UC-003 — enhanced with auto-fetch |
