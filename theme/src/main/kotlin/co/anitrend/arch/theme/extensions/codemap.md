# theme/src/main/kotlin/co/anitrend/arch/theme/extensions/

## Responsibility

Context extension helpers that expose the current themed environment.

## Design Patterns

Thin resource facade over boolean resources: night mode, light status bar, and light navigation bar.

## Data & Control Flow

Callers invoke the extension on a `Context`; the function reads the compiled boolean from the active resource qualifier.

## Integration Points

Backed by `state.xml` in values and night resource folders. Intended for app shell or UI code that configures system bars.
