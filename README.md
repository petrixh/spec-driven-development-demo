# My Application README

- [ ] TODO Replace or update this README with instructions relevant to your application

## Build & Run Commands

```bash
./mvnw                            # Run in dev mode (default goal: spring-boot:run)
./mvnw clean package              # Production build (JAR in target/)
./mvnw test                       # Run all tests
./mvnw test -Dtest=ClassName      # Run a single test class
```

The app runs on port 8080 (configurable via `PORT` env var).

To build a Docker image, run:

```bash
docker build -t my-application:latest .
```

If you use commercial components, pass the license key as a build secret:

```bash
docker build --secret id=proKey,src=$HOME/.vaadin/proKey .
```

## Getting Started

The [Quick Start](https://vaadin.com/docs/v25/getting-started/quick-start) tutorial helps you get started with Vaadin in
around 10 minutes. This tutorial walks you through building a simple application, introducing the core concepts along
the way.
