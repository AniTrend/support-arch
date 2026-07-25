# recycler/src/main/kotlin/co/anitrend/arch/recycler/adapter/controller/contract/

## Responsibility

Abstract adapter controller contract and load state listener type alias.

## Design Patterns

Template abstraction for header, footer, and combined ConcatAdapter construction.

## Data & Control Flow

Controllers accept load state changes, dispatch current state to new listeners, and expose factory methods for load state header or footer adapters.

## Integration Points

Implemented by `SupportAdapterController` and used through `SupportAdapter.controller`.
