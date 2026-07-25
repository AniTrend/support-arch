# buildSrc/src/main/java/co/anitrend/arch/buildSrc/module/

## Responsibility

Defines canonical support-arch module identifiers in `Modules.Support` and formats module paths with `Module.path()`.

## Design Patterns

Enum-backed registry avoids repeating raw project names across build configuration code.

## Data & Control Flow

Plugin helper functions compare `Project.name` to these identifiers. Dokka source-link configuration can turn identifiers into Gradle project paths.

## Integration Points

Used by `plugin/extensions/ProjectExtensions.kt` and `plugin/components/ProjectDokka.kt`.
