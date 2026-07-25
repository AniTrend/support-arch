# extension/src/main/kotlin/co/anitrend/arch/extension/dispatchers/contract/

## Responsibility

Defines the dispatcher bundle contract.

## Design Patterns

`ISupportDispatcher` exposes main, computation, IO, and confined `CoroutineDispatcher` properties.

## Data & Control Flow

Implementations provide dispatcher instances that downstream components use for threading decisions.

## Integration Points

Implemented by `SupportDispatcher` and required by data source base classes.
