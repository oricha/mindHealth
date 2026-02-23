# MindHealth — Wellness Events Platform

MindHealth is a comprehensive Spring Boot application that enables wellness enthusiasts to discover, register for, and purchase tickets to wellness events. The platform connects event organizers with users interested in meditation, yoga, alternative therapy, and other wellness activities.

## What the Application Does

### For Users
- **Discover events** — Browse popular events on the home page and search with autocomplete suggestions
- **Search and filter** — Find events by name, category, location, date, and price
- **Event details** — View full event information including description, date, time, location, price, organizer info, and embedded maps
- **Secure ticket purchase** — Check out with Stripe payment processing
- **Digital tickets** — Receive PDF tickets with unique QR codes by email (stored in AWS S3)
- **Reviews and ratings** — Leave and view event reviews (1–5 stars) after attending
- **Account management** — Register via email/password, update profile, view purchase history

### For Organizers
- **Create and manage events** — Add events with images, categories, location, pricing, and attendee limits
- **Attendee management** — View attendee lists and communicate with them via email
- **Analytics** — See tickets sold, revenue, and event performance

### For Administrators
- **Category management** — Define and manage event categories for navigation and filtering

### Technical Highlights
- **Authentication**: Spring Security with form login and JWT for API access
- **Payments**: Stripe integration for PCI-compliant payment processing
- **Storage**: AWS S3 for ticket PDFs; PostgreSQL for data
- **Notifications**: Email confirmations, ticket delivery, event reminders (1 week before), organizer messages
- **Stack**: Spring Boot 3, Java 21, Thymeleaf, PostgreSQL, Flyway migrations

## Prerequisites
- [Java 21](https://adoptium.net/)
- [Node.js](https://nodejs.org/) 22 (for frontend dev server; Gradle downloads Node for builds)
- [Docker](https://www.docker.com/get-started/) and Docker Compose (for database)

### Environment Variables
Create an `application-local.yml` or use a `.env` file. Key variables:
- **Database**: `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USERNAME`, `DB_PASSWORD`
- **Auth**: `JWT_SECRET`, `SPRING_PROFILES_ACTIVE=local` (or `dev`)
- **Stripe**: `STRIPE_API_KEY`, `STRIPE_WEBHOOK_SECRET`
- **AWS S3**: `AWS_ACCESS_KEY_ID`, `AWS_SECRET_ACCESS_KEY`, `AWS_REGION`, `S3_BUCKET_NAME`
- **Mail**: `SMTP_HOST`, `SMTP_PORT`, `SMTP_USERNAME`, `SMTP_PASSWORD` (e.g. [MailHog](https://github.com/mailhog/MailHog) for local dev)

## Quick Start

### 1. Start Dependencies
```bash
docker-compose up -d db
```
This starts PostgreSQL on port 5433 (or 5432 if available). The app will connect to it.

### 2. Run the Application
```bash
./gradlew bootRun
```
The default profile is `local`. In IntelliJ, add `-Dspring.profiles.active=local` in Run Configuration VM options.

### 3. Optional: Frontend Dev Server (Hot Reload)
For live reload of TypeScript, CSS, and templates:
```bash
npm install          # First time and after dependency updates
npm run devserver
```
Access the app at `http://localhost:8081` (dev server proxies API to the backend on 8080).

## Build Commands

| Command | Description |
|---------|-------------|
| `./gradlew build` | Full build: compiles Java, runs tests, bundles JS/CSS via npm, produces JAR |
| `./gradlew clean build` | Clean build |
| `./gradlew bootJar -x test` | Build runnable JAR without running tests |
| `./gradlew test` | Run unit and integration tests |
| `./gradlew jacocoTestReport` | Generate coverage report at `build/reports/jacoco/test/html/index.html` |

Node.js is downloaded automatically by the Gradle Node plugin; JS/CSS assets are bundled into the final JAR during `build`.

## Run in Production

```bash
./gradlew bootJar
java -Dspring.profiles.active=production -jar build/libs/mindhealth-0.0.1-SNAPSHOT.jar
```

Or build a Docker image:
```bash
./gradlew bootBuildImage --imageName=com.mindhealth/mindhealth
docker run --env-file .env -p 8080:8080 com.mindhealth/mindhealth
```
Set `SPRING_PROFILES_ACTIVE=production` in the container environment.

## Docker Compose (Full Stack)
```bash
docker-compose up -d
```
Starts the app and PostgreSQL together. Configure via `.env` and `docker-compose.yml`.

## Key Endpoints
- **App**: `http://localhost:8080/`
- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **Event search**: `GET /api/events/search?q=...`
- **Autocomplete**: `GET /api/events/autocomplete?q=...`

## Project Structure
- `src/main/java/com/mindhealth/mindhealth` — domain, DTOs, repositories, services, controllers, config
- `src/main/resources/templates` — Thymeleaf views
- `src/main/resources/static`, `ts/` — CSS, TypeScript (Webpack entry: `ts/app.ts`)
- `src/main/resources/db/migration` — Flyway migrations

## Troubleshooting

### "password authentication failed for user postgres"

**Local development (Docker Postgres):** The PostgreSQL data volume may have been created with different credentials. Reset it:

```bash
docker-compose down -v       # Remove containers and volumes
docker-compose up -d db      # Recreate with credentials from .env
```

Ensure your `.env` (or environment) has `DB_PASSWORD=postgres` (or match whatever you set). The app and Docker Postgres must use the same password.

**Using prod profile with AWS RDS:** Your `.env` may have `SPRING_PROFILES_ACTIVE=prod`, which connects to RDS. The `RDS_PASSWORD` in `.env` must match the password you set when creating the RDS instance. If it was never "postgres", update `RDS_PASSWORD` in `.env`.

**For local development**, use the dev or local profile so the app connects to localhost:

```bash
SPRING_PROFILES_ACTIVE=dev ./gradlew bootRun
```

Or in IntelliJ: set `SPRING_PROFILES_ACTIVE=dev` in Run Configuration environment variables (and avoid loading a `.env` that sets `prod`).

### nodeSetup fails ("Couldn't follow symbolic link" or similar)

The Node.js cache may be corrupted. Remove it and retry:

```bash
rm -rf .gradle/nodejs
./gradlew bootRun
```

**Alternative:** Use your system Node (if installed) to skip the download:

```bash
USE_SYSTEM_NODE=true ./gradlew bootRun
```

### Flyway migration failed (e.g. "relation does not exist")

If a migration failed, reset the database and retry (for local dev):

```bash
docker-compose down -v
docker-compose up -d db
# Then run the app again
```

## Further Reading
- Specification and design: `.agent/specs/wellness-events-platform/` — `design.md`, `requirements.md`, `tasks.md`
