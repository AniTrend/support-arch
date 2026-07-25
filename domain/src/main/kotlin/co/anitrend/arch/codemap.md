# domain/src/main/kotlin/co/anitrend/arch/

## Responsibility

Architecture package segment that contains the domain package.

## Design Patterns

Navigation-only codemap. It keeps traversal explicit and points to the child package that owns the implementation details.

## Data & Control Flow

No runtime data or control flow is implemented here. Continue to `domain/src/main/kotlin/co/anitrend/arch/domain/` for the module map.

## Integration Points

Integrated through the Android library source-set and package path only.
