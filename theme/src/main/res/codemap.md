# theme/src/main/res/

## Responsibility

Android resources for the theme module.

## Design Patterns

Resource qualifier pattern: base values provide defaults, night and API qualified folders override color and system bar behavior, drawable files provide reusable shape backgrounds.

## Data & Control Flow

Android resource resolution selects values by device mode, screen size, and API. Kotlin helpers and downstream modules read the generated `R` identifiers.

## Integration Points

Consumed through `co.anitrend.arch.theme.R` by recycler, ui, and apps applying `SupportTheme` or `SupportTheme3`.
