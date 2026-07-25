# request/src/main/kotlin/co/anitrend/arch/request/queue/

## Responsibility

Holds the mutable queue entry for one request identity.

## Design Patterns

`RequestQueue` stores the request, the last failed wrapper, the last passed wrapper, and an optional running callback.

## Data & Control Flow

The helper updates these fields as requests start, finish, fail, succeed, or are retried.

## Integration Points

Owned by `AbstractRequestHelper` and manipulated by `RequestHelper`.
