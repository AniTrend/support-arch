# processor/src/main/kotlin/co/anitrend/arch/processor/codegen/extension/

## Responsibility

Provides symbol-analysis helpers for code generation.

## Design Patterns

Kotlin extension function on `KSClassDeclaration` keeps annotation argument lookup close to symbol usage.

## Data & Control Flow

`annotationArgOf` flattens annotation arguments on a class declaration and returns the first argument matching the caller predicate.

## Integration Points

Used by `NavParamCodeGenerator` to inspect `NavParam.enabled`.
