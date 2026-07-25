# data/src/main/kotlin/co/anitrend/arch/

## Responsibility

Architecture package segment that contains the data package.

## Design Patterns

Navigation-only codemap. It keeps traversal explicit and points to the child package that owns the implementation details.

## Data & Control Flow

No runtime data or control flow is implemented here. Continue to `data/src/main/kotlin/co/anitrend/arch/data/` for the module map.

## Integration Points

Integrated through the Android library source-set and package path only.
