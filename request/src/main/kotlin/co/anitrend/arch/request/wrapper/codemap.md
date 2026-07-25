# request/src/main/kotlin/co/anitrend/arch/request/wrapper/

## Responsibility

Wraps request execution blocks so they can be invoked and retried consistently.

## Design Patterns

`RequestWrapper` stores the callback block, helper, and request. Its invoke operator creates a `RequestCallback`; `retry` delegates back to `runIfNotRunning`.

## Data & Control Flow

The helper creates wrappers during execution and stores successful or failed wrappers for later retry by status.

## Integration Points

Used by `RequestHelper`, `RequestCallback`, and request queue entries.
