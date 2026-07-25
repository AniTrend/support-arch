# processor/src/main/kotlin/co/anitrend/arch/processor/model/

## Responsibility

Holds lightweight data models used during generation. `Spec` carries the target package name and file name for generated output.

## Design Patterns

Immutable Kotlin data class used as a value object between processor analysis and code generation.

## Data & Control Flow

`NavParamCodeGenerator` receives a `Spec` and uses it to create both the KotlinPoet `FileSpec` and the KSP output file.

## Integration Points

Used by `codegen/NavParamCodeGenerator.kt`.
