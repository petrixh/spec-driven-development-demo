# Architecture

> Technology stack and application structure. `pom.xml` is the source of truth for versions. Do not modify `pom.xml` without asking.

---

## 1. Technology Stack

- Vaadin Flow (Aura theme) — server-side Java UI
- Spring Boot — auto-configuration, embedded Tomcat
- Java
- Maven (wrapper included)
- Database: H2 (in-memory) — embedded database with seed data for the concept demo
- Testing: JUnit 5 (via spring-boot-starter-test)

---

## 2. Application Structure

```
com.example.specdriven/
  Application.java              — Spring Boot entry point
  tickets/
    TicketBrowseView.java       — Vaadin @Route: browse available tickets
    TicketDetailView.java       — Vaadin @Route: ticket details + quantity
    TicketCheckoutView.java     — Vaadin @Route: order summary + CC form
    TicketConfirmationView.java — Vaadin @Route: confirmation + UUID
    TicketLookupView.java       — Vaadin @Route: lookup purchase by UUID
    TicketService.java          — Business logic (Spring @Service)
    TicketRepository.java       — Data access (Spring Data JPA)
    PurchaseService.java        — Purchase/order logic (Spring @Service)
    PurchaseRepository.java     — Purchase persistence (Spring Data JPA)
    Ticket.java                 — JPA entity
    PurchaseOrder.java          — JPA entity
    DataSeeder.java             — Populates H2 with sample ticket data on startup
```

- Application CSS: `src/main/resources/META-INF/resources/styles.css`

> **Prerequisite:** `pom.xml` needs `spring-boot-starter-data-jpa` and `h2` dependencies added before implementation.
