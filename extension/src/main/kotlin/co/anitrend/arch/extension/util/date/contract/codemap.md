# extension/src/main/kotlin/co/anitrend/arch/extension/util/date/contract/

## Responsibility

Defines the shared date helper contract and conversion utilities.

## Design Patterns

`AbstractSupportDateHelper` requires current season and year behavior, provides default input and output patterns, and implements Unix timestamp and date string conversion using ThreeTen backport types.

## Data & Control Flow

Date strings and timestamps flow through `DateTimeFormatter`, `ZoneId`, and target `TimeZone` arguments to produce formatted strings or epoch milliseconds.

## Integration Points

Extended by `SupportDateHelper`; depends on `SeasonType` and ThreeTenABP initialization from `ThreeTenInitializer`.
