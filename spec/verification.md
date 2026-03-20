# Verification

> Visual verification process using Playwright MCP, plus a per-use-case checklist.
> Copy the checklist (section 3) for each implemented use case.

---

## 1. Visual Verification Process

Use the Playwright MCP server to visually verify **every view** after implementation — both public React views and admin Vaadin Flow views.

### When to Verify

- After implementing a use case
- After changing styles, theme, or layout
- After adding Spring Security or any change that affects routing/rendering

### Steps

1. **Ensure the application is running**
2. **Navigate to every route** defined in the use case's UI/Routes section
   - For admin views: log in as `admin`/`admin` first, then navigate to the admin route
   - For public views: navigate without authentication
3. **Walk through the main flow** — perform each step from the use case's Main Flow
4. **Take screenshots** — capture the page state at key interaction points
5. **Check theme correctness** (see § Design System Compliance below)
6. **Check visual appearance** (take a screenshot and inspect it for each item):
   - Layout matches expectations (spacing, alignment, sizing)
   - Typography is readable and consistent
   - **Text contrast** — for every distinct text element visible in the screenshot:
     1. Use `browser_evaluate` to read the **computed colour** (`color`) and **computed background colour** (`background-color`) of the element from the DOM.
     2. Calculate the WCAG 2.1 contrast ratio from those two values.
     3. Report each element in a table with columns: **Element**, **Text colour (hex)**, **Background colour (hex)**, **Contrast ratio**, **WCAG AA result** (pass ≥ 4.5 for normal text, ≥ 3.0 for large text).
     Do not estimate colours by looking at the screenshot — always read the actual computed styles. Do not batch-approve; list each element separately. Flag any element that fails WCAG AA.
   - Interactive elements are clearly identifiable
   - Responsive behaviour works at common breakpoints (mobile, tablet, desktop)
7. **Record results** — note any visual issues in the per-use-case checklist below

### Design System Compliance

The design system (`design-system.md`) specifies different themes for different view types:

- **Public views** (React): dark mode — dark backgrounds (`#141414`), light text (`#FFFFFF`, `#B3B3B3`)
- **Admin views** (Vaadin Flow): default Aura theme — light backgrounds, dark text

Verification must confirm:

- [ ] Admin views render with the **default (light) Aura theme** — not dark backgrounds
- [ ] Public views render with the **dark theme** as defined in `styles.css`
- [ ] Custom dark CSS does **not** bleed into admin views (check that admin text is dark-on-light, not light-on-dark)
- [ ] Vaadin component text (Grid cells, form labels, buttons) is legible in admin views

---

## 2. Automated Testing

Every use case must have browserless tests before it is considered implemented. See `architecture.md` § Testing for setup conventions.

### Requirements

- Each acceptance criterion should be covered by at least one test
- Business rules must have dedicated tests (especially edge cases like limits, validation, and error handling)
- Tests must pass in CI (`./mvnw test`) before the use case status is set to **Implemented**

### Test Naming

- Test class: `[FeatureName]Test.java` (e.g., `BrowseMoviesTest`, `BuyTicketsTest`)
- Test methods: descriptive names that map to acceptance criteria or business rules (e.g., `onlyMoviesWithFutureShowsAreDisplayed`, `maximumSixSeatsPerTransaction`)

---

## 3. Per-Use-Case Verification Checklist

> Copy this section for each use case. Name it: **UC-[NNN]: [Feature Title]**

### UC-[NNN]: [Feature Title]

**Use case spec:** [`use-case-NNN-name.md`](use-cases/use-case-NNN-name.md)
**Verified by:** [Name/Agent]
**Date:** [YYYY-MM-DD]

#### Automated Tests

- [ ] Test class exists and all tests pass (`./mvnw test -Dtest=ClassName`)
- [ ] Acceptance criteria covered by tests
- [ ] Business rule edge cases tested

#### Functional

- [ ] Main flow works end-to-end as described in the spec
- [ ] All business rules are enforced (list BR-IDs: [BR-01, BR-02, ...])
- [ ] All acceptance criteria pass
- [ ] Error/edge cases handled appropriately

#### Visual

- [ ] **Theme correctness** — view uses the correct theme per design system (dark for public, default/light for admin)
- [ ] Page layout matches expectations
- [ ] All text is legible — for each distinct text element, use `browser_evaluate` to read the computed `color` and `background-color`, calculate the WCAG 2.1 contrast ratio, and report in a table: **Element | Text colour (hex) | Background colour (hex) | Contrast ratio | WCAG AA result**. Do not estimate colours visually; always read computed styles. Do not bulk-approve; list each element separately. Flag any element below 4.5:1 (normal text) or 3.0:1 (large text).
- [ ] Interactive elements respond correctly (hover, focus, click)
- [ ] Loading states and transitions are smooth
- [ ] Responsive at mobile and desktop widths

#### Result

- **Status:** [Pass / Fail / Partial]
- **Notes:** [Any issues found or follow-up items]
