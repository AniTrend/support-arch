# request/src/main/kotlin/co/anitrend/arch/request/report/

## Responsibility

Provides immutable status report views over a request.

## Design Patterns

`RequestStatusReport` implements `IRequestStatusReport` and exposes convenience checks for running, error, idle, success, request type, and request-specific error.

## Data & Control Flow

Helpers prepare reports after request status changes. Listeners inspect the report to translate status into UI load states.

## Integration Points

Constructed by `AbstractRequestHelper.prepareStatusReportLocked` and consumed by request listeners.
