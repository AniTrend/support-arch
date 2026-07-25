# recycler/src/main/kotlin/co/anitrend/arch/recycler/extensions/

## Responsibility

Small adapter utility extensions.

## Design Patterns

Type checked extension over `ISupportAdapter` to normalize emptiness checks across RecyclerView adapter implementations.

## Data & Control Flow

The extension inspects `itemCount` for ListAdapter or RecyclerView.Adapter implementations and throws if the adapter type is unsupported.

## Integration Points

Used by ui list presenters and fragments to decide whether to show full screen state or adapter load state rows.
