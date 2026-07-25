# data/src/main/kotlin/co/anitrend/arch/data/common/

## Responsibility

Holds the generic response wrapper contract for coroutine request execution.

## Design Patterns

`ISupportResponse<I, O>` is a functional interface shape with a suspend `invoke` operator.

## Data & Control Flow

Callers pass a resource and `RequestCallback`; implementations return an optional result and are responsible for recording success or failure through the callback.

## Integration Points

Integrates with `request.callback.RequestCallback` and can be used by repository implementations that need a consistent request wrapper.
