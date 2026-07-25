# request/src/main/kotlin/co/anitrend/arch/request/extension/

## Responsibility

Adapts request helper listener events into coroutine flows and internal load-state helpers.

## Design Patterns

Uses `callbackFlow` to register a `RequestHelperListener` and remove it on close. Internal helpers map request type to load position and extract request errors from reports.

## Data & Control Flow

Collectors subscribe to `createStatusFlow`; request status reports are converted to `LoadState` by the listener and emitted until the flow is closed.

## Integration Points

Used by `data.source.core.contract.AbstractDataSource` to expose `Flow<LoadState>` from a request helper.
