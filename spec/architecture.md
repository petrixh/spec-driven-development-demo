# Architecture

> Technology stack and application structure. `pom.xml` is the source of truth for versions. Do not modify `pom.xml` without asking.

---

## 1. Technology Stack

- Vaadin Flow (Aura theme) — server-side Java UI
- Spring Boot — auto-configuration, embedded Tomcat
- Java
- Maven (wrapper included)
- Database: [e.g., PostgreSQL, H2]
- Testing: JUnit 5, Vaadin Browserless Tests (`browserless-test-junit6`), Vitest for React views

---

## 2. Application Structure

```
com.example.specdriven/
  Application.java              — Spring Boot entry point
  [feature-package]/
    [FeatureView].java          — Vaadin @Route view
    [FeatureService].java       — Business logic (Spring @Service)
    [FeatureRepository].java    — Data access (Spring Data)
```

- Application CSS: `src/main/resources/META-INF/resources/styles.css`

---

## 3. Testing

- **Browserless Tests**: Vaadin Browserless Testing (`SpringBrowserlessTest`)
  - Tests live in `src/test/java/`, mirroring the main package structure
  - Extend `SpringBrowserlessTest`, annotate with `@SpringBootTest`
  - Use `@WithMockUser(roles = "ADMIN")` for admin views
  - Use `@WithAnonymousUser` for access control tests
  - Use `navigate(ViewClass.class)` to render views
  - Use `$(ComponentClass.class)` to query components, `test(component)` to interact
- **React View Tests**: Vitest with React Testing Library
  - Tests live in `src/test/frontend/`, mirroring the view structure
  - Mock `@BrowserCallable` endpoint calls
  - Test component rendering, user interactions, and navigation
  - Run via `npx vitest run`
- **Test Coverage**: Every use case must have UI tests
  - Vaadin Flow views → Browserless tests
  - React views → Vitest tests
  - See `verification.md` § Automated Testing for coverage requirements and naming conventions
- **Visual Verification**: Playwright MCP during development (not automated)
