# processor/src/main/kotlin/co/anitrend/arch/processor/

## Responsibility

Contains `NavParamProcessor`, the KSP processor that discovers `@NavParam` class declarations and coordinates source generation.

## Design Patterns

- Processor orchestration separated from file emission.
- Annotation lookup uses the qualified name from the annotation module.
- Symbols are grouped by parent declaration so each parent class receives one generated parameter object.

## Data & Control Flow

`process` gets symbols with `@NavParam`, filters them to `KSClassDeclaration`, logs the count, groups by parent declaration, and invokes `NavParamCodeGenerator` for each group. It returns an empty list because no symbols are deferred.

## Integration Points

- Uses `co.anitrend.arch.annotation.NavParam`.
- Calls into `codegen/NavParamCodeGenerator.kt`.
- Instantiated by `provider/NavParamProcessorProvider.kt`.
