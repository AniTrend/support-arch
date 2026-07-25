# extension/src/main/kotlin/co/anitrend/arch/extension/dispatchers/

## Responsibility

Provides the default dispatcher bundle used by request and data infrastructure.

## Design Patterns

`SupportDispatcher` is a data class implementing `ISupportDispatcher` with main, default, IO, and single-thread confined dispatchers.

## Data & Control Flow

Consumers inject or subclass dispatcher bundles so work can be routed to the appropriate coroutine dispatcher.

## Integration Points

Consumed by `data.source.core.contract.AbstractDataSource` to configure request helper main and IO execution contexts.
