# data/src/main/kotlin/co/anitrend/arch/data/mapper/

## Responsibility

Provides the base mapper type for response-to-storage mapping workflows.

## Design Patterns

`SupportResponseMapper<S, D>` is an abstract class that implements the mapper contract without adding behavior, giving consumers a stable extension point.

## Data & Control Flow

Implementations map a source response to a destination type, then insert the mapped data into storage through contract methods.

## Integration Points

Uses `mapper/contract` for the required operations. Repository implementations subclass this package to keep mapping and persistence steps together.
