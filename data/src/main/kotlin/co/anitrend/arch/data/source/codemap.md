# data/src/main/kotlin/co/anitrend/arch/data/source/

## Responsibility

Groups data source contracts and reusable source implementations.

## Design Patterns

Uses interface contracts for required capabilities and abstract base classes for shared request helper wiring.

## Data & Control Flow

Data sources invalidate local storage, expose load-state flow, run refresh and retry operations, and delegate request state tracking to `request` helpers.

## Integration Points

Depends on `domain` for load state, `extension` for dispatchers, and `request` for request helper behavior.
