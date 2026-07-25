# annotation/

## Responsibility

Publishes the JVM-only annotation API used by the KSP processor. The module currently defines `NavParam`, which marks navigation argument classes for generated parameter constants.

## Design Patterns

- Marker annotation with a small configuration flag.
- Binary retention keeps annotation metadata available to KSP without requiring runtime reflection.

## Data & Control Flow

Consumers annotate classes with `@NavParam`. During compilation, the processor module reads those symbols and generates companion parameter files when `enabled` is true.

## Integration Points

- Applies the shared `co.anitrend.arch` plugin and the Kotlin JVM plugin.
- Exported to `processor` through a project dependency.
- Meaningful package map: `src/main/kotlin/co/anitrend/arch/annotation/codemap.md`.
