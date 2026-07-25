# request/src/main/kotlin/co/anitrend/arch/request/

## Responsibility

Package root for request helper abstractions and implementations.

## Design Patterns

`AbstractRequestHelper` owns listener registration and report dispatch. Child packages provide callbacks, contracts, exceptions, models, queues, wrappers, status reports, and load-state flow adapters.

## Data & Control Flow

Requests are deduplicated by request id and type, executed through wrappers, reported by callbacks, stored as success or failure wrappers, then replayed by status when refresh or retry is requested.

## Integration Points

Consumed by `data.source.core` for load-state flow creation and retry behavior. Uses `domain.entities` for `RequestError` and `LoadState`.
