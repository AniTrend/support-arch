# recycler/src/main/

## Responsibility

Main source set for recycler Kotlin APIs and load state row layouts.

## Design Patterns

Kotlin adapter stack plus XML layouts bound through generated view binding classes.

## Data & Control Flow

Domain models are mapped into `IRecyclerItem`, bound through `SupportViewHolder`, and emitted clicks through `ClickableItem` flows. Load state values drive header and footer rows.

## Integration Points

Depends on extension, core, theme, domain, Material Components, SwipeRefreshLayout, and RecyclerView. Used by ui and recycler-paging-legacy.
