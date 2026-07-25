# paging-legacy/src/main/kotlin/co/anitrend/arch/paging/legacy/

## Responsibility

Legacy Paging 2 package for Flow based PagedList building and invalidation callbacks.

## Design Patterns

Builder pattern around `PagedList.Builder` plus internal callback cleanup contract.

## Data & Control Flow

`FlowPagedListBuilder.buildFlow` creates a channel flow, creates a data source, builds a PagedList on the fetch dispatcher, emits on the notify dispatcher, and recreates lists when data sources invalidate.

## Integration Points

Uses AndroidX Paging 2, Kotlin coroutines, and Timber. Data source packages provide request aware boundary callbacks.
