# recycler/src/main/kotlin/co/anitrend/arch/recycler/common/

## Responsibility

Common event model for RecyclerView item and state interactions.

## Design Patterns

Sealed class event hierarchy with typed payload variants and click type enum.

## Data & Control Flow

View holders and load state rows publish `ClickableItem` values through flows. List fragments observe `ClickableItem.State` to trigger retry behavior.

## Integration Points

Shared by recycler adapters, ui state layout, and ui list fragments. State events carry domain `LoadState`.
