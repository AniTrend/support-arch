# data/src/main/kotlin/co/anitrend/arch/data/converter/

## Responsibility

Provides a reusable two-way converter base class.

## Design Patterns

`SupportConverter<M, E>` implements `ISupportConverter` by delegating single-item conversion to protected `fromType` and `toType` lambdas.

## Data & Control Flow

Consumers provide the lambdas. Collection conversion is inherited from the contract and maps each item through the single-item conversion functions.

## Integration Points

Used by data and storage layers that need reversible model-to-entity conversion.
