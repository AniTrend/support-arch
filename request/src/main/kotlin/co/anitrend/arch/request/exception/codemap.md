# request/src/main/kotlin/co/anitrend/arch/request/exception/

## Responsibility

Defines explicit exceptions for request callback misuse.

## Design Patterns

`RequestException` is a sealed `IllegalStateException`; `ResultAlreadyRecorded` represents duplicate success or failure recording.

## Data & Control Flow

Control flow throws when a request block calls `recordSuccess` or `recordFailure` more than once for the same callback.

## Integration Points

Used by `RequestCallback` and visible to consumers that need to catch invalid request reporting.
