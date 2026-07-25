# paging-legacy/src/main/kotlin/co/anitrend/arch/paging/legacy/util/

## Responsibility

Default Paging 2 configuration constants.

## Design Patterns

Prebuilt `PagedList.Config` value object using shared default page size.

## Data & Control Flow

Consumers can use `PAGING_CONFIGURATION` when constructing legacy paged lists or Flow builders.

## Integration Points

Uses AndroidX Paging `PagedList.Config` and extension `DEFAULT_PAGE_SIZE`.
