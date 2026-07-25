# data/src/main/kotlin/co/anitrend/arch/data/mapper/contract/

## Responsibility

Defines the response mapper contract.

## Design Patterns

`ISupportResponseMapper<S, D>` separates transformation from database insertion using `onResponseMapFrom` and `onResponseDatabaseInsert`.

## Data & Control Flow

Data enters as source type `S`, is converted to destination type `D`, then is persisted by the consumer implementation.

## Integration Points

Implemented by `SupportResponseMapper` and repository-specific mapper classes.
