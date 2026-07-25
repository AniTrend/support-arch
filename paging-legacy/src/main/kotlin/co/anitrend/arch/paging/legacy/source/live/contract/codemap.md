# paging-legacy/src/main/kotlin/co/anitrend/arch/paging/legacy/source/live/contract/

## Responsibility

Abstract request aware `PageKeyedDataSource` base.

## Design Patterns

Contract composition for direct Paging 2 data sources with load state reporting.

## Data & Control Flow

Subclasses provide dispatchers and implement Paging 2 load callbacks. The base owns a RequestHelper and exposes a load state flow.

## Integration Points

Extended by `SupportPagingLiveDataSource`; integrates AndroidX Paging 2 with request helper status flow.
