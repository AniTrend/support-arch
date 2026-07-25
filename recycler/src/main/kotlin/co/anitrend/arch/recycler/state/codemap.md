# recycler/src/main/kotlin/co/anitrend/arch/recycler/state/

## Responsibility

State manager for top and bottom load state tracking.

## Design Patterns

Small state machine copied from AndroidX paging concepts. Tracks start and end load state separately.

## Data & Control Flow

`setState` updates top or bottom state, resets the opposite side to idle, skips duplicate values, then calls `onStateChanged`. New listeners receive both current states.

## Integration Points

Used by `SupportAdapterController`; state values come from domain `LoadState`.
