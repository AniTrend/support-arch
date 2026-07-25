# annotation/src/main/kotlin/co/anitrend/arch/annotation/

## Responsibility

Contains `NavParam`, the public annotation used to mark navigation argument classes for generated parameter constants.

## Design Patterns

- `@Target(AnnotationTarget.CLASS)` limits usage to classes.
- `@Retention(AnnotationRetention.BINARY)` supports compile-time processors without runtime lookup.
- The `enabled` flag lets a class opt out while retaining the annotation in source.

## Data & Control Flow

Annotated classes are discovered by `NavParamProcessor`. When `enabled` is true, the processor can emit a constant for the annotated class name into a generated `*Param` object.

## Integration Points

- Consumed by `processor/src/main/kotlin/co/anitrend/arch/processor`.
- Published as the stable annotation API for downstream KSP users.
