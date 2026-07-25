# request/src/main/kotlin/co/anitrend/arch/request/contract/

## Responsibility

Defines the request helper API and listener callback contract.

## Design Patterns

`IRequestHelper` exposes listener registration, guarded execution, result recording, retry by status, and status lookup. Nested `Listener` receives status reports.

## Data & Control Flow

Implementations accept request blocks, emit status changes, retain retryable wrappers, and notify listeners through `IRequestStatusReport`.

## Integration Points

Implemented by `AbstractRequestHelper` and `RequestHelper`; consumed by `RequestCallback`, `RequestWrapper`, and data sources.
