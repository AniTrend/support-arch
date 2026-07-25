# recycler-paging-legacy/src/main/

## Responsibility

Main source set for Paging 2 RecyclerView integration.

## Design Patterns

Adapter module that mirrors `SupportListAdapter` behavior on top of `PagedListAdapter`.

## Data & Control Flow

Paged lists are submitted to the adapter, items are mapped to `IRecyclerItem`, holders are bound through recycler contracts, and load state header or footer composition comes from the shared controller.

## Integration Points

Depends on extension, recycler, AndroidX Paging runtime, and RecyclerView.
