# ui/src/main/kotlin/co/anitrend/arch/ui/extension/

## Responsibility

Widget setup extensions for recycler views and swipe refresh layouts.

## Design Patterns

Extension function pattern for common view configuration.

## Data & Control Flow

`SupportRecyclerView.setUpWith` configures fixed size, nested scrolling, layout manager, and adapter. SwipeRefreshLayout helpers configure theme colors and stop refreshing after responses.

## Integration Points

Uses theme integer resources, extension color attribute helper, recycler view class, and Material color attributes.
