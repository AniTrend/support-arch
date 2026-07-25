# recycler/src/main/kotlin/co/anitrend/arch/recycler/model/

## Responsibility

Recycler item base class and related model packages.

## Design Patterns

Abstract item model wraps an id and supplies default no selection behavior plus a no op decorator.

## Data & Control Flow

Adapters map domain objects into `RecyclerItem` or `IRecyclerItem` instances. Holders bind those items and ask them for span size and cleanup behavior.

## Integration Points

Used across recycler adapters and shared load state rows. Selection behavior connects to action contracts and decorators.
