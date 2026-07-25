# recycler/src/main/kotlin/co/anitrend/arch/recycler/action/

## Responsibility

Selection related contracts and decorators for RecyclerView item action mode behavior.

## Design Patterns

Contract and decorator split: selection state behavior lives in contracts while visual updates live in decorator implementations.

## Data & Control Flow

View holders pass item ids and decorators into a selection mode implementation. The selection mode decides whether click or long click is consumed, and decorators update selected visuals.

## Integration Points

Used by `IRecyclerItem`, `SupportViewHolder`, and consumer supplied action mode implementations.
