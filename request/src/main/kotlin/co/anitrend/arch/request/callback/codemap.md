# request/src/main/kotlin/co/anitrend/arch/request/callback/

## Responsibility

Contains the callback used by executing request blocks to report completion.

## Design Patterns

`RequestCallback` guards result recording with an atomic flag so each request records either success or failure once.

## Data & Control Flow

Executing code receives a callback, calls `recordSuccess` or `recordFailure`, and the callback delegates to `IRequestHelper.recordResult` with the wrapper and optional `RequestError`.

## Integration Points

Created by `RequestWrapper` and consumed by repository or response code that runs inside `RequestHelper.runIfNotRunning`.
