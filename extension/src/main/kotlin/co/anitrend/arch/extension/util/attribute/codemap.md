# extension/src/main/kotlin/co/anitrend/arch/extension/util/attribute/

## Responsibility

Defines small attribute-like value enums used by utility helpers.

## Design Patterns

`SeasonType` enumerates winter, spring, summer, and fall.

## Data & Control Flow

Date helpers return these values when calculating the current season.

## Integration Points

Used by `SupportDateHelper` and `AbstractSupportDateHelper`.
