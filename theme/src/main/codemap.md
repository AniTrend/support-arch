# theme/src/main/

## Responsibility

Main source set for the theme module, including Kotlin theme helpers and Android XML resources.

## Design Patterns

Split package and resource structure. Kotlin exposes runtime helpers and animators, while resources define tokens consumed by other UI modules.

## Data & Control Flow

Runtime helpers read booleans and drawables from `res`; XML resources are compiled into `co.anitrend.arch.theme.R` and referenced by recycler and ui.

## Integration Points

Integrates with Material Components and is depended on by recycler and ui for selection frames, dimensions, color attributes, and grid span integers.
