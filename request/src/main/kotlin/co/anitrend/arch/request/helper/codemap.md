# request/src/main/kotlin/co/anitrend/arch/request/helper/

## Responsibility

Contains the concrete request helper implementation.

## Design Patterns

`RequestHelper` serializes queue mutations on the main dispatcher, executes request work on the IO dispatcher, and uses atomic holders around cross-dispatcher results.

## Data & Control Flow

`runIfNotRunning` creates or reuses a queue, marks running, dispatches a report, executes the wrapper, and stores run status. `recordResult` stores success or failure wrappers. `retryWithStatus` replays stored wrappers and can run a pre-retry action.

## Integration Points

Depends on `RequestQueue`, `RequestWrapper`, `IRequestStatusReport`, `Request`, `RequestError`, and Timber logging. Used by `data` source base classes.
