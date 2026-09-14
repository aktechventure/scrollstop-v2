# Architecture

## Local-first architecture

ScrollStop is a local-first Android application focused on preventing unconscious app usage through a deterministic local intervention loop. The app does not depend on a backend or cloud service.

The architectural flow is:

- Compose UI
- ViewModel
- UseCase
- Repository
- Data source / Room / UsageStats

## Core runtime components

### UI

The Compose screens in the `presentation` package render onboarding, home, stats, mindful, and settings states. The UI is intentionally limited to the existing V0.1 product scope.

### Room persistence

The Room database stores:

- app usage records
- intervention sessions
- reflections
- insights
- user preferences

The database is app-private and local to the device. Data deletion is handled through the repository and database transaction path.

### UsageStats

The app reads Android usage data via `UsageStatsManager` through `UsageStatsDataSource` to compute daily metrics and app-level usage summaries.

### AccessibilityService

`ScrollStopAccessibilityService` is a system accessibility service used to detect relevant app-window changes and trigger the intervention overlay. It is intentionally limited to the monitored package set used by the app.

### OverlayController

`OverlayController` is responsible for showing and hiding the intervention prompt overlay. Its semantics are kept minimal and user-visible without introducing new product behavior.

### Deterministic intervention engine

The app uses a deterministic decision engine to gate interventions by:

- threshold minutes
- targeted-app allowlist
- quiet-hours enforcement
- active session deduplication
- local reflection persistence

This engine remains local and does not depend on external services or AI.

## Release build process

The app is built as a standard Android Application module. Release configuration includes minification and resource shrinking. The project does not ship with production signing secrets and expects release credentials to be provided externally through environment variables or Gradle properties.
