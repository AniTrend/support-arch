# data/src/main/kotlin/co/anitrend/arch/data/source/core/contract/

## Responsibility

Defines the shared abstract base for data sources.

## Design Patterns

`AbstractDataSource` combines `IDataSource` and `ISource`, requires an `ISupportDispatcher`, lazily creates a `RequestHelper`, and exposes a status flow.

## Data & Control Flow

Subclasses provide dispatchers and clearing behavior. Request state changes from `RequestHelper` are converted into `Flow<LoadState>` through `createStatusFlow`.

## Integration Points

Connects `extension.dispatchers`, `request.helper.RequestHelper`, and `request.extension.createStatusFlow` to data-layer source contracts.
