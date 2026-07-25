# recycler/src/main/kotlin/co/anitrend/arch/recycler/holder/contract/

## Responsibility

ViewHolder contract for binding recycler item models and clearing resources.

## Design Patterns

Interface boundary around holder behavior.

## Data & Control Flow

Adapters call `bind` with position, payloads, item model, click flow, and optional selection mode; RecyclerView recycle calls `onViewRecycled`.

## Integration Points

Implemented by `SupportViewHolder` and used by adapter contracts.
