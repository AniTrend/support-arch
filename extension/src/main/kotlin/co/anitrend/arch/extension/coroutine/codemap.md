# extension/src/main/kotlin/co/anitrend/arch/extension/coroutine/

## Responsibility

Defines coroutine scope contracts and the internal implementation used by scope factories.

## Design Patterns

`ISupportCoroutine` combines `CoroutineScope`, a supervisor job, dispatcher, scope, and child cancellation helper. `SupportCoroutine` composes a job and dispatcher into a coroutine context.

## Data & Control Flow

Factories create scoped wrappers. Consumers launch work through `scope` and can cancel child jobs through `cancelAllChildren`.

## Integration Points

Factory functions live in `coroutine/extension`. Other modules can depend on the interface instead of raw coroutine scope construction.
