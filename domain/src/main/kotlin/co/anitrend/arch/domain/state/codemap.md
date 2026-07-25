# domain/src/main/kotlin/co/anitrend/arch/domain/state/

## Responsibility

Defines the generic UI state contract shared by repository and presentation layers.

## Design Patterns

`UiState<T>` is an abstract class that requires a load-state value plus suspendable `refresh` and `retry` callbacks.

## Data & Control Flow

Concrete states provide the observable model and bind user-triggered refresh or retry actions back to the data source layer.

## Integration Points

`data.state.DataState` extends this contract with `Flow<LoadState>` and a model flow.
