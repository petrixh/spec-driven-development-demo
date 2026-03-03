# Architecture

> Technology stack, non functional requirements, and application structure. `pom.xml` is the source of truth for versions. Do not modify `pom.xml` without asking.

---

## 1. Technology Stack

- Vaadin Flow (Aura theme) — server-side Java UI
- Spring Boot — auto-configuration, embedded Tomcat
- Java
- Maven (wrapper included)
- Database: [e.g., PostgreSQL, H2]
- Testing: [e.g., JUnit 5, TestBench]

---

## 2. Non-Functional Requirements

[Describe the non functional requirements, such as availability, security, performance, internationalization.]

### [NFR 1]

- [Quality 1]
- [Quality 2]
- [How to know the NFR has been met, if known]
- [Tactics for meeting the NFR, if known]

### [NFR 2]

- [Quality 1]
- [Quality 2]
- [How to know the NFR has been met, if known]
- [Tactics for meeting the NFR, if known]

---

## 3. Application Structure

```
com.example.specdriven/
  Application.java              — Spring Boot entry point
  [feature-package]/
    [FeatureView].java          — Vaadin @Route view
    [FeatureService].java       — Business logic (Spring @Service)
    [FeatureRepository].java    — Data access (Spring Data)
```

- Application CSS: `src/main/resources/META-INF/resources/styles.css`
