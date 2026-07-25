# extension/src/main/kotlin/co/anitrend/arch/extension/util/time/

## Responsibility

Provides elapsed-time checking helpers and time unit values.

## Design Patterns

`SupportTimeHelper` is experimental and compares a current instant with another instant using a reference amount and `SupportDateTimeUnit`. `SupportTimeInstant` is a `Long` alias.

## Data & Control Flow

`hasElapsed` subtracts the provided instant from the current instant, converts milliseconds to the requested unit, and compares against the reference amount.

## Integration Points

Uses `SupportExperimental` and Java `TimeUnit`; useful for cache staleness and throttling checks.
