# core/

## Responsibility

Core Android support abstractions for presenters, WorkManager workers, file provider helpers, and UI state contracts shared by higher modules.

## Design Patterns

Android library module using the shared `co.anitrend.arch` Gradle plugin. Public APIs are grouped under `src/main/kotlin`, resources under `src/main/res`, and an empty manifest declares no module components.

## Data & Control Flow

Consumers depend on the Gradle module, then call Kotlin APIs or reference generated resources through the module namespace.

## Integration Points

See child codemaps for source packages, resources, and module specific dependency notes.
