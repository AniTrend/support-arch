# analytics/

## Responsibility

Defines the analytics abstraction for Android consumers. The module currently exposes the `ISupportAnalytics` contract and an empty manifest under the `co.anitrend.arch.analytics` namespace.

## Design Patterns

- Interface boundary for analytics and crash reporting providers.
- Consumer-owned implementation, this module does not bind to a concrete analytics SDK.

## Data & Control Flow

Callers pass screen state, exceptions, log messages, and crash identifiers into an `ISupportAnalytics` implementation. Implementations decide how to forward those calls to logging, analytics, or crash reporting backends.

## Integration Points

- Applies the shared `co.anitrend.arch` Gradle plugin.
- Uses Android `Bundle` and `Log` types in the public contract.
- Meaningful package map: `src/main/kotlin/co/anitrend/arch/analytics/contract/codemap.md`.
