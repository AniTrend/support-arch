# paging-legacy/src/main/kotlin/co/anitrend/arch/paging/legacy/builder/

## Responsibility

Abstract contract for building `Flow<PagedList<V>>` from a `DataSource.Factory` and `PagedList.Config`.

## Design Patterns

Abstract builder pattern with configurable initial load key, boundary callback, notify dispatcher, and fetch dispatcher.

## Data & Control Flow

Concrete builders provide the data source factory and config, then `buildFlow` emits PagedLists while preserving initial or previous list keys across invalidations.

## Integration Points

Implemented by `FlowPagedListBuilder`. Integrates with AndroidX Paging and coroutines.
