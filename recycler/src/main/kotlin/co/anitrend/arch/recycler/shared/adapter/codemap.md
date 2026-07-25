# recycler/src/main/kotlin/co/anitrend/arch/recycler/shared/adapter/

## Responsibility

Adapter that renders `LoadState` as a single RecyclerView row when loading or error states are active.

## Design Patterns

Specialized `ListAdapter` implementing `ISupportAdapter<LoadState>` with a mapper from load state to support item models.

## Data & Control Flow

Changing `loadState` compares previous and new displayability, then inserts, removes, or refreshes index 0. Binding selects loading, error, or default layout and forwards click events.

## Integration Points

Composed as headers and footers by `SupportAdapterController`. Used by ui list fragments for retry rows.
