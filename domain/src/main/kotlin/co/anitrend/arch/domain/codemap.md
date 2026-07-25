# domain/src/main/kotlin/co/anitrend/arch/domain/

## Responsibility

Package root for reusable domain contracts and state models.

## Design Patterns

Splits value objects under `entities` from UI-facing contracts under `state` so low-level state can be reused without presentation dependencies.

## Data & Control Flow

`entities` provides load and error values. `state` wraps observable load state plus refresh and retry callbacks for UI models.

## Integration Points

Consumed by `request` for request status translation and by `data` for `DataState` creation.
