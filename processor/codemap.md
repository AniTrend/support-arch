# processor/

## Responsibility

Provides the KSP implementation for `@NavParam`. It discovers annotated classes and generates Kotlin source files containing string constants for navigation argument class names.

## Design Patterns

- KSP provider and processor entry points.
- Dedicated code generator behind the `ICodeGenerator` interface.
- KotlinPoet file construction for generated Kotlin objects.

## Data & Control Flow

KSP creates `NavParamProcessorProvider`, which builds `NavParamProcessor` with the environment logger and code generator. Each round resolves `@NavParam` class declarations, groups them by parent declaration, and passes each group to `NavParamCodeGenerator`. The generator writes a `ParentParam.kt` file for each parent class group.

## Integration Points

- Depends on `:annotation`, KSP API, and KotlinPoet.
- Registered through the KSP plugin in `build.gradle.kts`.
- Meaningful package maps live under `src/main/kotlin/co/anitrend/arch/processor`.
