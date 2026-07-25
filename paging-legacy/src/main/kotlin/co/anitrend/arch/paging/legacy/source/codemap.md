# paging-legacy/src/main/kotlin/co/anitrend/arch/paging/legacy/source/

## Responsibility

Boundary callback based paging data sources for `PagedList` flows backed by repositories or local storage.

## Design Patterns

Template class extending a request aware abstract boundary callback. `SupportPagingDataSource` owns a `SupportPagingHelper` and delegates request state to `RequestHelper`.

## Data & Control Flow

`invalidate` clears the data source on the IO dispatcher and resets paging helper page state. `retryFailed` asks RequestHelper to rerun failed requests. `refresh` invalidates.

## Integration Points

Depends on data source contracts, extension dispatchers and paging helper, and request lifecycle helpers.
