# ui/src/main/kotlin/co/anitrend/arch/ui/fragment/list/contract/

## Responsibility

Contract for list fragments that provide adapters, state configuration, layout managers, and refresh handling.

## Design Patterns

Interface boundary extending `SwipeRefreshLayout.OnRefreshListener`.

## Data & Control Flow

Implementations supply a `SupportAdapter`, `StateLayoutConfig`, load state observer, and functions to install layout manager and adapter on `SupportRecyclerView`.

## Integration Points

Implemented by `SupportFragmentList`; consumed by `SupportListPresenter`.
