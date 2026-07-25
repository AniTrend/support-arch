# recycler/src/main/kotlin/co/anitrend/arch/recycler/adapter/contract/

## Responsibility

Core adapter contract used by list, paging, and load state adapters.

## Design Patterns

Interface with default behavior for span lookup, animation, binding by item type, stable ids, and refresh notification.

## Data & Control Flow

The adapter maps each item into `IRecyclerItem`, binds it into a holder with a mutable click flow and optional selection mode, then optionally starts a custom animator.

## Integration Points

Implemented by `SupportListAdapter`, legacy paging adapters, and `SupportLoadStateAdapter`; depends on core state config, theme animator contracts, and recycler model contracts.
