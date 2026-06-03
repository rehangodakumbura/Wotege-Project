# Project Skill Guide

Use this repository as a full-stack hotel and restaurant app with a Spring Boot backend and a Vite/React frontend.

## Backend Goals

- Keep the backend under `Backend/demo`.
- Build and run the backend with the Maven wrapper.
- Treat Spring Boot as the source of truth for API contracts, validation, and persistence.
- Prefer REST endpoints under `/api` with predictable request and response shapes.
- Keep database credentials out of source files. Read PostgreSQL settings from environment variables.

## PostgreSQL Setup

- Use PostgreSQL 17 locally unless the user requests a different version.
- Configure the backend to connect with environment variables such as `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USER`, and `DB_PASSWORD`.
- Do not hardcode the password in code, markdown, or committed config files.
- The password supplied by the user should be stored only in local secrets or an untracked `.env` file.
- Prefer migrations for schema changes so tables, indexes, and constraints stay reproducible.

## Backend Build Workflow

- Inspect `Backend/demo/pom.xml` before changing dependencies.
- After backend changes, validate with the Maven wrapper.
- Use the smallest possible API, service, repository, and entity changes needed for the feature.
- Keep controllers thin and move business logic into services.
- Add CORS only for the frontend origin that actually needs access.

## Frontend Integration With RTK Query

- Use Redux Toolkit Query for all backend data access.
- Create a dedicated API slice for hotel, restaurant, inventory, customer, staff, report, and authentication endpoints.
- Configure the frontend base URL from an environment variable such as `VITE_API_URL`.
- Keep API concerns in a single layer: endpoints in RTK Query, UI state in React components, and shared types in a small type module if needed.
- Use tags and invalidation for mutations that should refresh lists after create, update, or delete actions.
- Show loading, success, and error states in the UI for every networked screen.

## Data Flow Rules

- React components should call RTK Query hooks rather than raw `fetch`.
- Backend responses should be stable and consistent so the frontend can rely on them.
- If a field is required in the UI, validate it on the backend too.
- When adding a new resource, update the backend endpoint, the RTK Query slice, and the consuming page together.

## Maintenance Rules

- Keep schema changes backward-compatible when possible.
- Prefer environment-driven configuration over checked-in secrets.
- When the database structure changes, update the migration history and any seeded data.
- Document any new API route, DTO, or database table that the frontend depends on.

## Local Commands

- Backend build: `Backend/demo/mvnw.cmd test` on Windows.
- Backend package build: `Backend/demo/mvnw.cmd clean package` on Windows.
- Frontend install: run `npm install` from the `Frontend` folder.
- Frontend type check/build: `npm run lint` and `npm run build` from the `Frontend` folder.

## Implementation Priority

1. Stabilize the backend API and PostgreSQL configuration.
2. Add or update entities, repositories, services, and controllers.
3. Wire RTK Query into the frontend.
4. Connect screens to live backend data.
5. Verify the full flow end to end.