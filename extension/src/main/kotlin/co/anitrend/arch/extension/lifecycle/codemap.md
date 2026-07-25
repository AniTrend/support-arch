# extension/src/main/kotlin/co/anitrend/arch/extension/lifecycle/

## Responsibility

Defines a default lifecycle observer contract with optional logging callbacks.

## Design Patterns

`SupportLifecycle` extends `DefaultLifecycleObserver` and supplies default methods for create, start, resume, pause, stop, and destroy.

## Data & Control Flow

Consumers implement only the lifecycle callbacks they need, while default methods log transitions through Timber.

## Integration Points

Attached and detached through `LifecycleOwner.attachComponent` and `detachComponent` extension functions.
