# paging-legacy/src/main/kotlin/co/anitrend/arch/paging/legacy/source/live/

## Responsibility

Page keyed live data source support for Paging 2 use cases without a backing source.

## Design Patterns

Template subclass over a request aware `PageKeyedDataSource` base.

## Data & Control Flow

`invalidate` resets the paging helper and calls the framework invalidate. Retry and refresh delegate through RequestHelper and invalidation.

## Integration Points

Depends on the live contract package, extension paging helper, and request model status values.
