# ui/src/main/kotlin/co/anitrend/arch/ui/fragment/list/presenter/

## Responsibility

Presenter implementation for reusable list screens.

## Design Patterns

Presenter pattern with lifecycle callbacks. It configures recycler, swipe refresh, and state layout while the fragment handles data submission.

## Data & Control Flow

On create view, it pushes config into the state layout, attaches refresh listeners, installs adapter and layout manager, and enables nested scrolling. Load state changes go to adapter rows when content exists, otherwise to full screen state layout.

## Integration Points

Uses recycler empty checks, SupportRecyclerView, SwipeRefreshLayout helpers, domain load state, and state layout contracts.
