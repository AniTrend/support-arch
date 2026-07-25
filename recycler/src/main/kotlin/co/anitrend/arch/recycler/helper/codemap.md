# recycler/src/main/kotlin/co/anitrend/arch/recycler/helper/

## Responsibility

RecyclerView snap helper utilities.

## Design Patterns

Subclass of `PagerSnapHelper` with listener callback injection.

## Data & Control Flow

When RecyclerView settles on a target snap position, the helper notifies the listener with a one based page number.

## Integration Points

Uses AndroidX RecyclerView snap APIs and the helper contract package.
