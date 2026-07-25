# buildSrc/

## Responsibility

Builds the shared Gradle convention plugin exposed as `co.anitrend.arch`. It centralizes Android, Kotlin JVM, Dokka, Spotless, dependency, source-jar, and Maven publication defaults for support-arch modules.

## Design Patterns

- Convention plugin implemented with Gradle Kotlin DSL.
- Small component functions configure one build concern each.
- Module-name predicates select Android library behavior versus Kotlin JVM behavior.
- Version catalog access keeps dependency and plugin versions in `gradle/libs.versions.toml`.

## Data & Control Flow

When a module applies `co.anitrend.arch`, Gradle instantiates `CorePlugin`. The plugin applies required base plugins, configures Android only for non-JVM modules, then configures Dokka, source artifacts, dependencies, Spotless, and logs discovered extensions and components.

## Integration Points

- Plugin marker: `src/main/resources/META-INF/gradle-plugins/co.anitrend.arch.properties`.
- Reads versions and dependency aliases from `../gradle/libs.versions.toml`.
- Reads release metadata from `gradle/version.properties` through `PropertiesReader`.
- Uses `spotless/copyright.kt` as the license header.
