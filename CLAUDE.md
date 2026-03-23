# CLAUDE.md

## Build & Run Commands

```bash
./mvnw                            # Run in dev mode (default goal: spring-boot:run)
./mvnw clean package              # Production build (JAR in target/)
./mvnw test                       # Run all tests
./mvnw test -Dtest=ClassName      # Run a single test class
```

The app runs on port 8080 (configurable via `PORT` env var).

## Specifications

Project specs live in `spec/`. Read [`spec/README.md`](spec/README.md) first.

## Guardrails

- Do not modify `pom.xml` without asking
- Do not modify `vite.config.ts` without asking
- Do not modify `spec/architecture.md` without asking
- **When in doubt, consult the Vaadin MCP.** Whenever you are even slightly unsure about Vaadin API usage, component behavior, theme variables, styling, or best practices — use the Vaadin MCP server to look it up before guessing. Do not rely on memory for Vaadin specifics.
