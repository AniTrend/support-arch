# data/src/main/kotlin/co/anitrend/arch/data/converter/contract/

## Responsibility

Defines the two-way converter contract.

## Design Patterns

`ISupportConverter<M, E>` requires single-item conversion in both directions and supplies default collection conversion helpers.

## Data & Control Flow

Callers can convert individual model or entity values, or collections, with the same contract.

## Integration Points

Implemented by `SupportConverter` and consumer-specific converters.
