# Architecture

> Technology stack and application structure. `pom.xml` is the source of truth for versions. Do not modify `pom.xml`, `vite.config.ts`, or `architecture.md` without asking.

---

## 1. Technology Stack

- Vaadin 25 — server-side Java UI (Vaadin Flow only, no React/Hilla views)
- Spring Boot — auto-configuration, embedded Tomcat
- Java 21+
- Maven (wrapper included)
- Database: H2 (in-memory, mocked data for demo)
- Routing: Vaadin Flow views use `@Route`
- Testing: JUnit 5, Vaadin Browserless Tests (`browserless-test-junit6`)

---

## 2. Application Structure

```
com.example.specdriven/
  Application.java              — Spring Boot entry point
  view/
    BookReturnView.java         — Vaadin @Route view (scan & process returns)
  service/
    BookReturnService.java      — Business logic (Spring @Service)
  repository/
    BookRepository.java         — Data access (Spring Data)
    PatronRepository.java       — Data access (Spring Data)
    LendingRecordRepository.java — Data access (Spring Data)
  model/
    Book.java                   — JPA entity
    Patron.java                 — JPA entity
    LendingRecord.java          — JPA entity
    LendingStatus.java          — Enum (CHECKED_OUT, RETURNED, etc.)
```

---

## 3. Testing

- **Browserless Tests**: Vaadin Browserless Testing (`SpringBrowserlessTest`)
  - Tests live in `src/test/java/`, mirroring the main package structure
  - Extend `SpringBrowserlessTest`, annotate with `@SpringBootTest`
  - Use `navigate(ViewClass.class)` to render views
  - Use `$(ComponentClass.class)` to query components, `test(component)` to interact
- **Service Tests**: JUnit tests for Spring `@Service` classes
  - Tests live in `src/test/java/`, same as browserless tests
  - Annotate with `@SpringBootTest`, autowire the service
  - Test business rules, validation, and data access
- **Test Coverage Requirements**:
  - Vaadin Flow views: Browserless view tests
  - Services: JUnit service tests
- **Visual Verification**: Playwright MCP during development (not automated)

---

## 4. Security

- No authentication or login required — all views are publicly accessible
