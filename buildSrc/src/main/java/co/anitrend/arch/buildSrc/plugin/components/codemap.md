# buildSrc/src/main/java/co/anitrend/arch/buildSrc/plugin/components/

## Responsibility

Contains the shared build configuration functions called by `CorePlugin`: Android defaults, dependency wiring, Dokka, Maven publication, plugin application, source artifacts, Spotless, and release property reading.

## Design Patterns

- One file per build concern keeps convention logic modular.
- Gradle extension accessors are centralized in `plugin/extensions`.
- `PropertiesReader` loads release metadata from `gradle/version.properties` on demand.

## Data & Control Flow

`CorePlugin.apply` invokes these functions in order. Android modules receive compile SDK, min SDK, Java 21, lint, test, build feature, packaging, and Kotlin compiler options. All modules receive Dokka, source artifacts, publishing metadata, default plugins, dependencies when eligible, and Spotless rules.

## Integration Points

- Reads the root version file and Spotless license header.
- Uses `Modules.Support` to map documentation dependencies.
- Uses Gradle Android, Kotlin, Dokka, Publishing, and Spotless extensions.
