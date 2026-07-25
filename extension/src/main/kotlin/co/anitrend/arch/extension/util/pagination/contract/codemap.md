# extension/src/main/kotlin/co/anitrend/arch/extension/util/pagination/contract/

## Responsibility

Defines the paging helper contract.

## Design Patterns

`ISupportPagingHelper` requires refresh, previous-page, next-page, and first-page checks.

## Data & Control Flow

Implementations mutate their own paging state in response to consumer paging operations.

## Integration Points

Implemented by `SupportPagingHelper`.
