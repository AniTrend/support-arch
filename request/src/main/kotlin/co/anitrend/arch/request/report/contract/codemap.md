# request/src/main/kotlin/co/anitrend/arch/request/report/contract/

## Responsibility

Defines the status report interface used by listeners.

## Design Patterns

`IRequestStatusReport` exposes status predicates, type lookup, and error lookup for a request type.

## Data & Control Flow

Listeners use the report without direct access to the helper queue internals.

## Integration Points

Implemented by `RequestStatusReport` and referenced by `IRequestHelper.Listener`.
