# Verification

> Visual verification process using Playwright MCP, plus a per-use-case checklist.
> Copy the checklist (section 3) for each implemented use case.

---

## 1. Visual Verification Process

Use the Playwright MCP server to visually verify **every view** after implementation.

### When to Verify

- After implementing a use case
- After changing styles, theme, or layout
- After any change that affects routing/rendering

### Default Browser Resolution

Unless the use case specifies a particular resolution or size, use **1920x1080** as the default browser resolution for all visual verification.

### Steps

1. **Ensure the application is running**
2. **Navigate to every route** defined in the use case's UI/Routes section
3. **Walk through the main flow** — perform each step from the use case's Main Flow
4. **Take screenshots** — capture the page state at key interaction points
5. **Check visual appearance** using the detailed checklist below:

   **Text & Readability**
   - No text is clipped, truncated, or overflowing its container
   - No text overlaps other text or UI elements
   - All text has sufficient contrast against its background (WCAG AA: 4.5:1 for normal text, 3:1 for large text)
   - Text on colored buttons/badges is readable (check disabled states too)
   - Long content (names, values) wraps or truncates gracefully with ellipsis

   **Spacing & Alignment**
   - No elements overlap or touch without intended spacing
   - Consistent padding inside containers (no edge-hugging content)
   - Aligned baselines for horizontally adjacent text/buttons
   - No unintended gaps or collapsed sections

   **Interactive Elements**
   - All buttons have visible boundaries or fill (not invisible against background)
   - Disabled elements are visually distinct but still readable
   - Focus ring visible when tabbing through elements
   - Hover states provide feedback on clickable elements
   - Touch targets are at least 48x48px

   **Dialogs & Overlays**
   - Dialog content doesn't overflow the dialog bounds
   - Background is visibly dimmed behind modal dialogs
   - Dialog is centered and appropriately sized

   **Responsive**
   - Check at 1920x1080 (desktop), 768x1024 (tablet), 390x844 (mobile)
   - No horizontal scrollbar at any breakpoint
   - Content reflows rather than shrinking to unreadable size
6. **Record results** — note any visual issues in the per-use-case checklist below

---

## 2. Automated Testing

Every use case must have UI tests before it is considered implemented. See `architecture.md` § Testing for tool setup and which test type to use per view type.

### Coverage Requirements

- Each acceptance criterion should be covered by at least one test
- Business rules must have dedicated tests (especially edge cases like limits, validation, and error handling)
- Tests must pass (`./mvnw test`) before the use case status is set to **Implemented**

### How to Write Tests

#### Browserless Tests (Vaadin Flow views)

- Tests live in `src/test/java/`, mirroring the main package structure
- Extend `SpringBrowserlessTest`, annotate with `@SpringBootTest`
- Use `@WithMockUser(roles = "ADMIN")` for admin views
- Use `@WithAnonymousUser` for access control tests
- Use `navigate(ViewClass.class)` to render views
- Use `$(ComponentClass.class)` to query components, `test(component)` to interact

#### React View Tests (Vitest)

- Tests live in `src/test/frontend/`, mirroring the view structure
- Mock `@BrowserCallable` endpoint calls
- Test component rendering, user interactions, and navigation
- Run via `npx vitest run`

### Naming Conventions

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

#### Visual — Text & Readability

- [ ] No text clipped, truncated, or overflowing its container
- [ ] No text overlapping other text or UI elements
- [ ] Sufficient contrast on all text (including on colored buttons, badges, disabled states)
- [ ] Long content wraps or shows ellipsis gracefully

#### Visual — Spacing & Alignment

- [ ] No elements overlapping or touching without intended spacing
- [ ] Consistent padding inside containers
- [ ] Aligned baselines for adjacent text/buttons
- [ ] No unintended gaps or collapsed sections

#### Visual — Interactive Elements

- [ ] All buttons have visible boundaries or fill
- [ ] Disabled elements are visually distinct but readable
- [ ] Focus ring visible when tabbing
- [ ] Touch targets at least 48x48px

#### Visual — Dialogs & Overlays

- [ ] Dialog content doesn't overflow dialog bounds
- [ ] Background dimmed behind modal dialogs
- [ ] Dialogs centered and appropriately sized

#### Visual — Responsive

- [ ] Desktop (1920x1080): layout correct
- [ ] Tablet (768x1024): content reflows appropriately
- [ ] Mobile (390x844): single-column, no horizontal scroll

#### Result

- **Status:** [Pass / Fail / Partial]
- **Notes:** [Any issues found or follow-up items]
