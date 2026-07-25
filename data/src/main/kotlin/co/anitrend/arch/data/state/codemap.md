# data/src/main/kotlin/co/anitrend/arch/data/state/

## Responsibility

Defines UI-ready data state for model flows and load-state flows.

## Design Patterns

`DataState<T>` extends `UiState<Flow<LoadState>>` and uses an internal constructor plus companion factory to bind an `IDataSource` to a model flow.

## Data & Control Flow

`IDataSource.create(model)` packages the data model flow, `loadState`, `refresh`, and `retryFailed` callbacks into one value for UI consumers.

## Integration Points

Depends on `domain.state.UiState`, `domain.entities.LoadState`, and `data.source.contract.IDataSource`.
