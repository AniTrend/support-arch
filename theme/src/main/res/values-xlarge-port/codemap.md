# theme/src/main/res/values-xlarge-port/

## Responsibility

Screen size and orientation overrides for list configuration integers in `values-xlarge-port`.

## Design Patterns

Resource qualifier override pattern for grid span and single list sizing.

## Data & Control Flow

Recycler and UI layout code reads integer resources from `co.anitrend.arch.theme.R`; Android chooses these values for matching devices.

## Integration Points

Supports responsive list spans in recycler and ui without hardcoding device checks in Kotlin.
