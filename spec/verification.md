# Verification

> Visual verification process using Playwright MCP, plus a per-use-case checklist.
> Copy the checklist (section 3) for each implemented use case.

---

## 1. Visual Verification Process

Use the Playwright MCP server to visually verify **every view** after implementation.

### When to Verify

- After implementing a use case
- After changing styles, theme, or layout
- After adding Spring Security or any change that affects routing/rendering

### Steps

1. **Ensure the application is running**
2. **Navigate to every route** defined in the use case's UI/Routes section
3. **Walk through the main flow** — perform each step from the use case's Main Flow
4. **Take screenshots** — capture the page state at key interaction points
5. **Check visual appearance:**
   - Layout matches expectations (spacing, alignment, sizing)
   - Typography is readable and consistent
   - Interactive elements are clearly identifiable
   - Responsive behaviour works at common breakpoints (mobile, tablet, desktop)
6. **Record results** — note any visual issues in the per-use-case checklist below

---

## 2. Automated Testing

Every use case must have UI tests before it is considered implemented. See `architecture.md` § Testing for tool setup and which test type to use per view type.

### Coverage Requirements

- Each acceptance criterion should be covered by at least one test
- Business rules must have dedicated tests (especially edge cases like limits, validation, and error handling)
- Tests must pass (`./mvnw test`) before the use case status is set to **Implemented**

### How to Write Tests

- **Test class**: `[FeatureName]Test.java` or `[FeatureName].test.tsx` (e.g., `BrowseMoviesTest`, `BuyTickets.test.tsx`)
- **Test methods**: descriptive names that map to acceptance criteria or business rules (e.g., `onlyItemsWithFutureEventsAreDisplayed`, `maximumSixItemsPerTransaction`)
- **Structure**: one test class per use case, with individual test methods for each acceptance criterion and business rule edge case

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

- [ ] Page layout matches expectations
- [ ] Interactive elements respond correctly (hover, focus, click)
- [ ] Loading states and transitions are smooth
- [ ] Responsive at mobile and desktop widths

#### Result

- **Status:** [Pass / Fail / Partial]
- **Notes:** [Any issues found or follow-up items]
