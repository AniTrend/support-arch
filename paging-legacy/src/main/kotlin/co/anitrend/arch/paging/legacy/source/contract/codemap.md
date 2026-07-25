# paging-legacy/src/main/kotlin/co/anitrend/arch/paging/legacy/source/contract/

## Responsibility

Abstract request aware `PagedList.BoundaryCallback` base.

## Design Patterns

Contract composition: AndroidX boundary callback plus repository source contracts from data.

## Data & Control Flow

Subclasses supply dispatchers. The base lazily creates a `RequestHelper` and exposes its status flow as `loadState` for UI observers.

## Integration Points

Extended by `SupportPagingDataSource`; depends on data `IDataSource` and `ISource`, extension dispatcher contract, and request helper APIs.
