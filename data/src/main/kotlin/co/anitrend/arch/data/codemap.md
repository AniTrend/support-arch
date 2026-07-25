# data/src/main/kotlin/co/anitrend/arch/data/

## Responsibility

Package root for data-layer abstractions used by repositories and view models.

## Design Patterns

Organizes response handling, conversion, mapping, source contracts, source base classes, UI state, and simple transformers into focused packages.

## Data & Control Flow

Request execution is coordinated by source classes. Responses can be mapped, inserted, converted, transformed, then exposed as `DataState` with model and load-state flows.

## Integration Points

Bridges `domain` value types, `extension` dispatchers, and `request` helpers. Higher-level modules consume these contracts rather than implementing request tracking directly.
