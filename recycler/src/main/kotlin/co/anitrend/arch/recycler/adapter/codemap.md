# recycler/src/main/kotlin/co/anitrend/arch/recycler/adapter/

## Responsibility

Adapter abstractions for standard list backed RecyclerView usage.

## Design Patterns

Mixin interface plus concrete abstract `ListAdapter` base. The controller handles load state composition while `SupportListAdapter` handles binding, stable ids, spans, and lifecycle cleanup.

## Data & Control Flow

Consumers subclass `SupportListAdapter`, provide a mapper and view holder creation. Data is submitted to ListAdapter, mapped to `IRecyclerItem`, bound to `SupportViewHolder`, and click events flow through `clickableFlow`.

## Integration Points

Integrates with recycler contracts, theme animators, core state configuration, extension layout inflater helpers, and AndroidX RecyclerView.
