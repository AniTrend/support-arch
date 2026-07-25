# buildSrc/src/main/

## Responsibility

Production source set for the convention plugin code and plugin marker resources.

## Design Patterns

Kotlin source files are under the `java` source root, with Gradle plugin metadata under `resources`.

## Data & Control Flow

The buildSrc build compiles plugin classes and packages the plugin marker so module build scripts can apply `id("co.anitrend.arch")`.

## Integration Points

See `java/co/anitrend/arch/buildSrc/codemap.md` for implementation details.
