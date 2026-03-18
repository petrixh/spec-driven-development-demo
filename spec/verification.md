# Verification

> Visual verification process using Playwright MCP, plus a per-use-case checklist.
> Copy the checklist (section 2) for each implemented use case.

---

## 1. Visual Verification Process

Use the Playwright MCP server to visually verify each use case after implementation.

### Steps

1. **Ensure the application is running**
2. **Navigate to the route** — open the page defined in the use case's UI/Routes section
3. **Walk through the main flow** — perform each step from the use case's Main Flow
4. **Take screenshots** — capture the page state at key interaction points
5. **Check visual appearance** (take a screenshot and inspect it for each item):
   - Layout matches expectations (spacing, alignment, sizing)
   - Typography is readable and consistent
   - **Text contrast** — for every distinct text element visible in the screenshot:
     1. Use `browser_evaluate` to read the **computed colour** (`color`) and **computed background colour** (`background-color`) of the element from the DOM.
     2. Calculate the WCAG 2.1 contrast ratio from those two values.
     3. Report each element in a table with columns: **Element**, **Text colour (hex)**, **Background colour (hex)**, **Contrast ratio**, **WCAG AA result** (pass ≥ 4.5 for normal text, ≥ 3.0 for large text).
     Do not estimate colours by looking at the screenshot — always read the actual computed styles. Do not batch-approve; list each element separately. Flag any element that fails WCAG AA.
   - Interactive elements are clearly identifiable
   - Responsive behaviour works at common breakpoints (mobile, tablet, desktop)
6. **Record results** — note any visual issues in the per-use-case checklist below

---

## 2. Per-Use-Case Verification Checklist

> Copy this section for each use case. Name it: **UC-[NNN]: [Feature Title]**

### UC-[NNN]: [Feature Title]

**Use case spec:** [`use-case-NNN-name.md`](use-cases/use-case-NNN-name.md)
**Verified by:** [Name/Agent]
**Date:** [YYYY-MM-DD]

#### Functional

- [ ] Main flow works end-to-end as described in the spec
- [ ] All business rules are enforced (list BR-IDs: [BR-01, BR-02, ...])
- [ ] All acceptance criteria pass
- [ ] Error/edge cases handled appropriately

#### Visual

- [ ] Page layout matches expectations
- [ ] All text is legible — for each distinct text element, use `browser_evaluate` to read the computed `color` and `background-color`, calculate the WCAG 2.1 contrast ratio, and report in a table: **Element | Text colour (hex) | Background colour (hex) | Contrast ratio | WCAG AA result**. Do not estimate colours visually; always read computed styles. Do not bulk-approve; list each element separately. Flag any element below 4.5:1 (normal text) or 3.0:1 (large text).
- [ ] Interactive elements respond correctly (hover, focus, click)
- [ ] Loading states and transitions are smooth
- [ ] Responsive at mobile and desktop widths

#### Result

- **Status:** [Pass / Fail / Partial]
- **Notes:** [Any issues found or follow-up items]
