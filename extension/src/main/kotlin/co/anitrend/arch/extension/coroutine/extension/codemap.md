# extension/src/main/kotlin/co/anitrend/arch/extension/coroutine/extension/

## Responsibility

Provides named factory functions for default coroutine scope wrappers.

## Design Patterns

`Default`, `Main`, and `Io` create `ISupportCoroutine` instances backed by a new `SupervisorJob` and the matching dispatcher.

## Data & Control Flow

Callers choose a dispatcher-specific factory and receive a reusable scope contract.

## Integration Points

Uses the internal `SupportCoroutine` implementation from the parent package.
