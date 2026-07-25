# paging-legacy/src/main/

## Responsibility

Main source set for legacy paging abstractions.

## Design Patterns

Paging 2 adapter support separated from data source support. This module focuses on data sources and builders, not RecyclerView adapters.

## Data & Control Flow

DataSource invalidation triggers new PagedLists or refreshes paging helpers. RequestHelper exposes load state flows for UI observation.

## Integration Points

Depends on data, domain, extension, request, and AndroidX Paging runtime and common artifacts.
