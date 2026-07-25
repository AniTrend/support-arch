# ui/src/main/kotlin/co/anitrend/arch/ui/view/contract/

## Responsibility

Shared custom view lifecycle contract.

## Design Patterns

Small interface for constructor initialization and optional recycle cleanup.

## Data & Control Flow

Custom views call `onInit` from constructors and call `onViewRecycled` when detached or recycled.

## Integration Points

Implemented by `SupportImageView` and `SupportStateLayout` contracts.
