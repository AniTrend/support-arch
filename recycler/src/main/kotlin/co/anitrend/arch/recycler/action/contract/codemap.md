# recycler/src/main/kotlin/co/anitrend/arch/recycler/action/contract/

## Responsibility

Contracts for item selection mode and action mode callbacks.

## Design Patterns

Interfaces abstract Android `ActionMode` behavior from adapter and item binding code.

## Data & Control Flow

Item clicks call `isSelectionClickable` or `isLongSelectionClickable`; selection changes are reported through `ISupportSelectionListener`.

## Integration Points

Implemented by consuming apps. Used by `SupportViewHolder` and `IRecyclerItem` binding.
