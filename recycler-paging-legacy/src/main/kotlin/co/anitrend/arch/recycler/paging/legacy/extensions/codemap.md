# recycler-paging-legacy/src/main/kotlin/co/anitrend/arch/recycler/paging/legacy/extensions/

## Responsibility

Emptiness helpers for paging adapters implementing support adapter contracts.

## Design Patterns

Type checked extension over `ISupportAdapter` for `PagedListAdapter` and `PagingDataAdapter` item counts.

## Data & Control Flow

Callers pass an optional empty count and receive whether the underlying paging adapter currently has that count.

## Integration Points

Used by consumers that need paging specific empty checks. Complements the non paging recycler extension.
