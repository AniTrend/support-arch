# data/

## Responsibility

Android library module for repository-side data abstractions. It connects domain state, request lifecycle helpers, converters, mappers, and data source contracts.

## Design Patterns

Uses contracts plus abstract base classes for extensibility, converter and mapper patterns for type transformation, and flow-backed UI state objects.

## Data & Control Flow

A data source owns a request helper, exposes `Flow<LoadState>`, clears local stores on invalidation, retries failed or successful requests, and can be wrapped into `DataState` for UI observation.

## Integration Points

Depends on `:domain`, `:extension`, and `:request`, plus AndroidX Paging artifacts. It is intended to be consumed by higher modules such as `core`, `recycler`, and app repositories.
