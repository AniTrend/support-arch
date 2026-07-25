# ui/src/main/kotlin/co/anitrend/arch/ui/fragment/list/

## Responsibility

Reusable list fragment base for standard lists and legacy paged lists.

## Design Patterns

Fragment plus presenter pattern. The fragment owns adapter and view model hooks, while `SupportListPresenter` owns widget and load state presentation.

## Data & Control Flow

Initialization attaches adapter and presenter to lifecycle, listens for retry clicks, fetches initial data when the adapter is empty, observes load state, and submits `List` or `PagedList` models to the matching support adapter.

## Integration Points

Composes core view model state, domain load state, recycler support adapters, recycler paging legacy adapter, and UI state layout.
