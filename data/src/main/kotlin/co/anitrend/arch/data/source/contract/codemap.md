# data/src/main/kotlin/co/anitrend/arch/data/source/contract/

## Responsibility

Defines public data source contracts.

## Design Patterns

`IDataSource` covers request helper access, load-state observation, refresh, and failed-request retry. `ISource` covers invalidation and clearing backing stores.

## Data & Control Flow

Consumers implement clear and invalidation behavior while shared base classes provide request queue and load-state wiring.

## Integration Points

Used by `source/core` and `data.state.DataState` to expose uniform refresh and retry callbacks.
