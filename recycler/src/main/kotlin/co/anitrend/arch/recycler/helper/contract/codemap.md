# recycler/src/main/kotlin/co/anitrend/arch/recycler/helper/contract/

## Responsibility

Callback contract for snap position changes.

## Design Patterns

Single method listener interface.

## Data & Control Flow

`SupportSnapHelper` calls `onPageChanged` when a target snap position is resolved.

## Integration Points

Implemented by consumers that need page indicators or analytics for snapping lists.
