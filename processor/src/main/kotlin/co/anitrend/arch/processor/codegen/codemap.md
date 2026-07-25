# processor/src/main/kotlin/co/anitrend/arch/processor/codegen/

## Responsibility

Generates Kotlin source for `@NavParam` groups. `NavParamCodeGenerator` emits an object named after the parent class plus `Param`.

## Design Patterns

- KotlinPoet builders isolate source text construction from file creation.
- KSP `Dependencies` are source-based and non-aggregating for incremental processing.
- `ICodeGenerator` provides an invocation contract for generator implementations.

## Data & Control Flow

The generator receives class declarations for one parent. It builds constant properties for declarations whose `enabled` annotation argument is true, creates a KSP file in the annotated package, writes the KotlinPoet file, logs generation, and reports write failures through the KSP logger.

## Integration Points

- Reads `NavParam.enabled` through `extension/annotationArgOf`.
- Uses `model/Spec` for package and file naming.
- Implements `contract/ICodeGenerator`.
