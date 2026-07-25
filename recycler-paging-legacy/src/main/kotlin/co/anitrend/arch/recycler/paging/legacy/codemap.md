# recycler-paging-legacy/src/main/kotlin/co/anitrend/arch/recycler/paging/legacy/

## Responsibility

Legacy RecyclerView paging package for `SupportPagedListAdapter` and adapter extensions.

## Design Patterns

Compatibility layer over recycler module contracts so Paging 2 adapters behave like standard support adapters.

## Data & Control Flow

PagedListAdapter receives paged data, delegates view holder creation and binding to `ISupportAdapter` defaults, and can be wrapped with load state rows through the shared controller.

## Integration Points

Used by ui `SupportFragmentList` when the model is a `PagedList`; depends on recycler module abstractions.
