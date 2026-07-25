# theme/

## Responsibility

Reusable Android theme assets for Material 2 and Material 3 colors, dimensions, styles, drawable backgrounds, configuration integers, and small theme helpers.

## Design Patterns

Android library module using the shared `co.anitrend.arch` Gradle plugin. Public APIs are grouped under `src/main/kotlin`, resources under `src/main/res`, and an empty manifest declares no module components.

## Data & Control Flow

Consumers depend on the Gradle module, then call Kotlin APIs or reference generated resources through the module namespace.

## Integration Points

See child codemaps for source packages and module specific dependency notes. Android resources are summarized here rather than mapped inside `src/main/res`, because non-XML files under resource directories are rejected by the Android resource merger.
