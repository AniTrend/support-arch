# extension/src/main/kotlin/co/anitrend/arch/extension/annotation/

## Responsibility

Defines module-wide opt-in annotations for experimental APIs.

## Design Patterns

`SupportExperimental` uses `RequiresOptIn` with warning level.

## Data & Control Flow

APIs annotated with it require explicit opt-in from consumers or call sites.

## Integration Points

Used by experimental utility APIs such as `SupportTimeHelper`.
