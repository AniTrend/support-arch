# theme/src/main/res/values-normal-land/

## Responsibility

Screen size and orientation overrides for list configuration integers in `values-normal-land`.

## Design Patterns

Resource qualifier override pattern for grid span and single list sizing.

## Data & Control Flow

Recycler and UI layout code reads integer resources from `co.anitrend.arch.theme.R`; Android chooses these values for matching devices.

## Integration Points

Supports responsive list spans in recycler and ui without hardcoding device checks in Kotlin.
