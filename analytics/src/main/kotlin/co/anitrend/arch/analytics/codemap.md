# analytics/src/main/kotlin/co/anitrend/arch/analytics/

## Responsibility

Package root for analytics abstractions.

## Design Patterns

The root delegates the concrete API surface to child packages, keeping the module namespace available without root-level types.

## Data & Control Flow

No behavior lives at the package root. Analytics calls enter through the `contract` package.

## Integration Points

See `contract/codemap.md`.
