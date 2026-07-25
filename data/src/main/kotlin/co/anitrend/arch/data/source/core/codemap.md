# data/src/main/kotlin/co/anitrend/arch/data/source/core/

## Responsibility

Provides the default flow-backed data source implementation.

## Design Patterns

`SupportCoreDataSource` extends `AbstractDataSource` and implements invalidation, failed retry, and refresh behavior.

## Data & Control Flow

`invalidate` clears the backing data source on the IO dispatcher. `retryFailed` reruns failed requests. `refresh` retries last successful requests after invalidation, then falls back to failed retry if nothing ran.

## Integration Points

Integrates with `request.model.Request.Status`, `RequestHelper.retryWithStatus`, and `extension.dispatchers.ISupportDispatcher` supplied by subclasses.
