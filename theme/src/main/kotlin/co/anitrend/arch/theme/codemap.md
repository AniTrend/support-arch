# theme/src/main/kotlin/co/anitrend/arch/theme/

## Responsibility

Public Kotlin package for theme runtime helpers, currently theme environment checks and animation abstractions.

## Design Patterns

Small extension functions for context backed resource lookups plus strategy style animator classes for RecyclerView item animations.

## Data & Control Flow

`Context` extension functions read boolean resources. Animator implementations create Android `Animator` arrays for a supplied `View` and expose duration plus interpolator metadata.

## Integration Points

Uses `co.anitrend.arch.theme.R`; consumed by recycler adapter animation and selection decoration code.
