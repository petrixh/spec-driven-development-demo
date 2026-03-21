# Architecture

> Technology stack and application structure. `pom.xml` is the source of truth for versions. Do not modify `pom.xml` without asking.

---

## 1. Technology Stack

- Vaadin Flow — server-side Java UI for all views (admin and public)
- Spring Boot — auto-configuration, embedded Tomcat
- Java
- Maven (wrapper included)
- Database: H2 (embedded, file-based for persistence across restarts)
- Routing: All views use Vaadin Flow `@Route`
- Testing: JUnit 5, Vaadin Browserless Tests (`browserless-test-junit6`)

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

## 4. Security & Admin

- **Spring Security** with `VaadinSecurityConfigurer`
- Public views: `@AnonymousAllowed` (React Hilla endpoints, public routes)
- Admin views: `@RolesAllowed("ADMIN")` (Vaadin Flow views)
- Login: Vaadin `LoginForm` at `/login`
- **Logout**: If the application has login functionality, authenticated views must include a visible logout link/button that ends the session and redirects to the public view.
- **Admin root view**: If the app has admin views, there must always be a root admin view at `/admin` that provides navigation to all admin sub-views (e.g., `/admin/products`). This serves as the admin landing page and ensures discoverability of all admin features.
