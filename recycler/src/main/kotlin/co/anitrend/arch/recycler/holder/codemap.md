# recycler/src/main/kotlin/co/anitrend/arch/recycler/holder/

## Responsibility

Shared ViewHolder implementation for recycler items.

## Design Patterns

ViewBinding backed holder that delegates bind and recycle behavior to `IRecyclerItem`.

## Data & Control Flow

The holder stores the current item, calls item `bind`, applies selection decoration when supported, and calls item `unbind` during recycle.

## Integration Points

Used by list adapters, legacy paging adapters, and load state adapters. Depends on item contracts and selection contracts.
