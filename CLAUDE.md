# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

SMP Query WebApp — a Java/Maven web application providing REST APIs and an admin UI for querying Peppol SMP (Service Metadata Publisher) endpoints. Built on the Helger Photon web framework with Bootstrap 4.

## Build & Run Commands

```bash
# Build WAR file
mvn clean install

# Run locally via embedded Jetty (port 8080)
# Execute RunInJettySMPQWA.main() from IDE, or:
mvn test -pl . -Dtest=com.helger.peppol.jetty.RunInJettySMPQWA

# Run tests
mvn test

# Docker build
docker build -t phelger/smpqwa .
```

## Architecture

### Web Layer (Servlet-based)
- **Root `/`** → redirects to `/public/default`
- **Public `/public/*`** → PublicApplicationServlet (no auth)
- **Secure `/secure/*`** → SecureApplicationServlet (login required via SecureLoginFilter)
- **API `/api/*`** → REST endpoints registered in `PPAPI.java`

### REST API Endpoints (PPAPI.java)
All return JSON. Registered most-specific-first:
- `GET /api/smpquery/{sml-id}/{participant-id}/{doctype-id}` — service information/endpoints
- `GET /api/smpquery/{sml-id}/{participant-id}` — document types for participant
- `GET /api/businesscard/{sml-id}/{participant-id}` — business card lookup
- `GET /api/ppidexistence/{sml-id}/{participant-id}` — DNS-based participant existence check

### Initialization
`AppWebAppListener` is the central bootstrap class — configures DNS, locales, AJAX handlers, REST API, menus, security (default admin/peppol), and UI settings.

### Key Framework Concepts
- **Photon (ph-oton)**: Helger's web framework providing Bootstrap UI components, security, menu system, AJAX/DataTables support, and servlet infrastructure
- **ph-config**: Configuration via `application.properties` with environment variable override support
- **xservlet-core**: Servlet request/response handling layer

### Package Layout (`com.helger.peppol`)
- `app/` — configuration (AppConfig), constants, security setup
- `servlet/` — servlet definitions and webapp listener
- `rest/` — REST API registration and handlers
- `ui/` — HTML layout, common UI config, form components
- `pub/` — public page handlers
- `secure/` — admin/secured page handlers

## Testing
- JUnit 4 with `ph-unittest-support-ext`
- `SPITest` validates SPI implementations
- Embedded Jetty runner (`RunInJettySMPQWA`) for local integration testing on port 8080

## CI/CD
GitHub Actions (`.github/workflows/maven.yml`): builds on push to main against Java 17, 21, 25. Java 17 build deploys SNAPSHOT to Sonatype.

## Configuration
`src/main/resources/application.properties` — key settings include `webapp.datapath`, SMP client truststore config, and optional Nemhandel SML support (`nemhandel.support.enabled`).
