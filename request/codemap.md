# request/

## Responsibility

Android library module for request lifecycle coordination, duplicate-run prevention, retry storage, status reporting, and load-state flow integration.

## Design Patterns

Uses queue objects keyed by request equality, callback reporting, wrapper objects for retry, listener dispatch, and sealed exception types for invalid callback usage.

## Data & Control Flow

A request runs through `RequestHelper.runIfNotRunning`, is wrapped in `RequestWrapper`, reports exactly one result through `RequestCallback`, updates queue state, dispatches status reports, and can be retried by status.

## Integration Points

Depends on `:domain` for `RequestError` and `LoadState`, and `:extension` for coroutine related infrastructure. `data` consumes this module through data sources and response wrappers.
