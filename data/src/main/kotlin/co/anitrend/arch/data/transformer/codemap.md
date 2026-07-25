# data/src/main/kotlin/co/anitrend/arch/data/transformer/

## Responsibility

Contains a minimal one-way transformation contract.

## Design Patterns

`ISupportTransformer<S, D>` defines a single synchronous `transform` function for source-to-destination conversion.

## Data & Control Flow

Data enters as `S` and returns as `D` without persistence or request lifecycle behavior.

## Integration Points

Useful for repository or presentation mapping where the fuller response mapper contract is not needed.
