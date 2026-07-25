# processor/src/main/kotlin/co/anitrend/arch/processor/codegen/contract/

## Responsibility

Defines the generator interface used by processor orchestration.

## Design Patterns

Functional interface style through `operator fun invoke`, accepting a group of `KSClassDeclaration` values.

## Data & Control Flow

The processor calls the generator with grouped symbols. Implementations decide how to transform those symbols into output.

## Integration Points

- Uses KSP `KSClassDeclaration`.
- Implemented by `NavParamCodeGenerator`.
