# buildSrc/src/main/java/co/anitrend/arch/buildSrc/

## Responsibility

Package root for the support-arch convention plugin implementation.

## Design Patterns

Implementation is split into module identifiers, plugin entry point, plugin components, extension helpers, and dependency strategy.

## Data & Control Flow

No executable behavior lives at this package root. `plugin/CorePlugin.kt` is the entry point and coordinates child package behavior.

## Integration Points

- `module/codemap.md` maps supported module identifiers.
- `plugin/codemap.md` maps plugin application and component wiring.
