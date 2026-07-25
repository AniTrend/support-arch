# extension/src/main/kotlin/co/anitrend/arch/extension/util/pagination/

## Responsibility

Provides mutable paging counter helpers.

## Design Patterns

`SupportPagingHelper` tracks page, page offset, page size, and paging-limit state while implementing `ISupportPagingHelper`.

## Data & Control Flow

Refresh resets to page one and offset zero. Previous and next move the page and offset by page size. `isFirstPage` checks initial position.

## Integration Points

Used by data source or UI paging code that needs lightweight offset bookkeeping.
