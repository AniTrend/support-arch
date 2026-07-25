# extension/src/main/kotlin/co/anitrend/arch/extension/util/date/

## Responsibility

Provides concrete date helper behavior.

## Design Patterns

`SupportDateHelper` extends `AbstractSupportDateHelper` and calculates current season plus current year with optional winter delta.

## Data & Control Flow

Consumers call season and year helpers directly, or inherited conversion methods for timestamp and date string transformations.

## Integration Points

Depends on `util/attribute.SeasonType` and the contract package. Runtime date conversion is supported by ThreeTenABP initialization.
