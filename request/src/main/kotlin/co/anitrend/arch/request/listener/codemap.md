# request/src/main/kotlin/co/anitrend/arch/request/listener/

## Responsibility

Contains the internal listener that converts status reports into load states.

## Design Patterns

`RequestHelperListener` implements `IRequestHelper.Listener` and writes to a `ProducerScope<LoadState>`.

## Data & Control Flow

On each status change it determines the load position, chooses loading, error, success, or idle state, then `trySend`s the state to the flow collector.

## Integration Points

Created by `createStatusFlow`; relies on `request.extension` helper functions and `domain.entities.LoadState`.
