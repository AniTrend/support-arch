# gradle/

## Responsibility

Stores shared Gradle inputs: dependency versions and aliases, project release metadata, Gradle wrapper settings, and daemon JVM toolchain metadata.

## Design Patterns

- Version catalog in `libs.versions.toml` centralizes plugin and library coordinates.
- `version.properties` provides release `version`, `code`, and `name` values for publication configuration.
- Wrapper and daemon property files pin Gradle distribution and Java toolchain behavior outside module build scripts.

## Data & Control Flow

Gradle reads the wrapper properties before running the build. buildSrc imports `libs.versions.toml` into its own `libs` catalog, and shared plugin code reads `version.properties` when configuring Maven publication metadata.

## Integration Points

- Consumed by root and module build scripts through Gradle version catalog accessors.
- Consumed by `buildSrc/settings.gradle.kts` and `buildSrc` plugin components.
- Gradle wrapper points to Gradle `9.6.1`, and daemon JVM metadata declares toolchain version `21`.
