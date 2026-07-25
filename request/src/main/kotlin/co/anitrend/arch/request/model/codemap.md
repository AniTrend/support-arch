# request/src/main/kotlin/co/anitrend/arch/request/model/

## Responsibility

Defines request identity and status models.

## Design Patterns

`Request` is a sealed class with `id`, `type`, mutable status, and last error. Equality is based on id and type. Enums model request type and lifecycle status.

## Data & Control Flow

Queues use request equality to deduplicate work. Helpers mutate status and last error as callbacks report results.

## Integration Points

Used throughout `request`, plus `data` refresh and retry logic through `Request.Status`.
