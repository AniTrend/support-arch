# extension/src/main/kotlin/co/anitrend/arch/extension/util/

## Responsibility

Groups small utility constants and helper packages for dates, time, seasons, and pagination.

## Design Patterns

Uses focused value objects and contracts instead of large utility classes. The top-level package currently exposes `DEFAULT_PAGE_SIZE`.

## Data & Control Flow

Callers use child helpers for time elapsed checks, date conversion, current season and year lookup, and mutable page tracking.

## Integration Points

Used by app and library layers that need shared utility behavior without adding higher-level dependencies.
