# recycler-paging-legacy/src/main/kotlin/co/anitrend/arch/recycler/paging/legacy/adapter/

## Responsibility

Paging 2 adapter base that plugs `PagedListAdapter` into support recycler contracts.

## Design Patterns

Abstract adapter template mirroring `SupportListAdapter`: stable id option, span sizing, view holder binding, recycling, and load state controller support.

## Data & Control Flow

Consumers subclass it, provide diff callback, mapper, state config, and holder creation. RecyclerView callbacks bind mapped items and clear animations on detach.

## Integration Points

Depends on AndroidX Paging `PagedListAdapter`, RecyclerView, extension inflater helpers, and recycler shared contracts.
