# Architecture

> Technology stack and application structure. `pom.xml` is the source of truth for versions. Do not modify `pom.xml` without asking.

---

## 1. Technology Stack

- Vaadin Flow — server-side Java UI
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

---

## 3. Testing

- **Browserless Tests**: Vaadin Browserless Testing (`SpringBrowserlessTest`) for Flow views
- **React View Tests**: Vitest with React Testing Library for React views
- **Test Coverage**: Every use case must have UI tests — see `verification.md` § Automated Testing for how to write them
- **Visual Verification**: Playwright MCP during development (not automated)
