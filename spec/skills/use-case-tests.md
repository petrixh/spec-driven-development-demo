# Writing Tests for a Use Case

This file describes how to write tests for a given use case

---

Each use case must be covered by the following tests:

## UI tests, one of
- **Browserless Tests**: Vaadin Browserless Testing (`SpringBrowserlessTest`)
  - If the view is implemented using Vaadin Flow
  - Tests live in `src/test/java/`, mirroring the main package structure
  - Extend `SpringBrowserlessTest`, annotate with `@SpringBootTest`
  - Use `@WithMockUser(roles = "ADMIN")` for admin views
  - Use `@WithAnonymousUser` for access control tests
  - Use `navigate(ViewClass.class)` to render views
  - Use `$(ComponentClass.class)` to query components, `test(component)` to interact
- **Vitest with React Testing Library**
  - If the view is implemented using React
  - Tests live in `src/test/frontend/`, mirroring the view structure
  - Mock `@BrowserCallable` endpoint calls
  - Test component rendering, user interactions, and navigation
  - Run via `npx vitest run`

## Backend / services tests
  - JUnit tests for Spring `@Service` classes
  - Tests live in `src/test/java/`, same as browserless tests
  - Annotate with `@SpringBootTest`, autowire the service
  - Test business rules, validation, and data access
  - Endpoints (`@BrowserCallable`) typically delegate to services — test the service, not the endpoint


## Coverage Requirements
- Each acceptance criterion should be covered by at least one test
- Business rules must have dedicated tests (especially edge cases like limits, validation, and error handling)

### Naming Conventions

- **Test class**: `[FeatureName]Test.java` or `[FeatureName].test.tsx` (e.g., `BrowseMoviesTest`, `BuyTickets.test.tsx`)
- **Test methods**: descriptive names that map to acceptance criteria or business rules (e.g., `onlyItemsWithFutureEventsAreDisplayed`, `maximumSixItemsPerTransaction`)
- **Structure**: one test class per use case, with individual test methods for each acceptance criterion and business rule edge case

---
