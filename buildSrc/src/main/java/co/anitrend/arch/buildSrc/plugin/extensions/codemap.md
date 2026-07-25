# buildSrc/src/main/java/co/anitrend/arch/buildSrc/plugin/extensions/

## Responsibility

Provides Gradle helper extensions for module classification, extension lookup, version catalog access, release properties, plugin detection, and dependency configuration shortcuts.

## Design Patterns

- Extension functions turn repetitive Gradle API calls into typed helpers.
- Module predicates compare project names against the `Modules.Support` registry.
- Dependency shortcuts map concise function names to Gradle configuration names.

## Data & Control Flow

Components call project helpers to obtain Gradle extensions, choose Android versus JVM behavior, access version catalog aliases, and add dependencies through the correct configurations.

## Integration Points

- Used by `CorePlugin`, component functions, and `DependencyStrategy`.
- Depends on Gradle, Android Gradle plugin, Kotlin Gradle plugin, Dokka, Spotless, and generated version catalog accessors.
