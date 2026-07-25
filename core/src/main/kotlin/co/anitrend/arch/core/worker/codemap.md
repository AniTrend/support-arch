# core/src/main/kotlin/co/anitrend/arch/core/worker/

## Responsibility

Abstract WorkManager worker bases for synchronous and coroutine background jobs.

## Design Patterns

Template method pattern over AndroidX `Worker` and `CoroutineWorker`; subclasses provide the work body by overriding `doWork`.

## Data & Control Flow

WorkManager instantiates the worker, calls `doWork`, and receives a `Result`. Coroutine workers execute suspending work on their coroutine context.

## Integration Points

Depends on AndroidX WorkManager. Consuming apps subclass these bases for job implementation.
