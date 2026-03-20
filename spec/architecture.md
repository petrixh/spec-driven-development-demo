# Architecture

> Technology stack and application structure. `pom.xml` is the source of truth for versions. Do not modify `pom.xml`, `vite.config.ts`, or `architecture.md` without asking.

---

## 1. Technology Stack

- Vaadin — server-side Java UI for admin views, client-side React for public views
- Spring Boot — auto-configuration, embedded Tomcat
- Java
- Maven (wrapper included)
- Database: H2 (embedded, file-persisted in dev, in-memory for tests)
- Routing: Vaadin Flow views use `@Route`. Hilla React views use file-based routing (`src/main/frontend/views/`), not `src/main/frontend/routes.tsx`.
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
- **Service Tests**: JUnit tests for Spring `@Service` classes
  - Tests live in `src/test/java/`, same as browserless tests
  - Annotate with `@SpringBootTest`, autowire the service
  - Test business rules, validation, and data access
  - Endpoints (`@BrowserCallable`) typically delegate to services — test the service, not the endpoint
- **Test Coverage Requirements**:
  - React views: Vitest view tests
  - Vaadin Flow views: Browserless view tests
  - Services: JUnit service tests
- **Visual Verification**: Playwright MCP during development (not automated)

---

## 4. UIState Management

- **Signals** are the primary mechanism for managing UI state
- **Non-shared signals** for standard per-user UI state (e.g., form values, selection state, view-local data)
- **Shared signals** when state must be visible across multiple users/sessions (collaborative or real-time features) — requires **server push** to be enabled
- When using shared signals, enable push on the view/UI (e.g., `@Push` annotation)

---

## 5. Security & Admin

- **Spring Security** with `VaadinSecurityConfigurer`
- In-memory admin user (`admin`/`admin`, role `ADMIN`) for development
- Public views: `@AnonymousAllowed` (React Hilla endpoints, public routes)
- Admin views: `@RolesAllowed("ADMIN")` (Vaadin Flow views)
- Login: Vaadin `LoginForm` at `/login`
- Admin index: `/admin` — links to all admin views (Manage Movies, Manage Shows)

---

## 5. Deployment

- **Fly.io** — used for manual testing deployments
- Deployed via `Dockerfile` (multi-stage build: JDK 21 build, JRE 21 Alpine runtime)
- H2 file-persisted database (non-persistent across deploys — acceptable for manual testing)
