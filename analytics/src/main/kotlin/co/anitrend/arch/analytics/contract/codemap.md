# analytics/src/main/kotlin/co/anitrend/arch/analytics/contract/

## Responsibility

Contains `ISupportAnalytics`, the public interface for screen-state logging, exception reporting, generic log events, session clearing, and crash identifier assignment.

## Design Patterns

- Contract-first abstraction with no default implementation.
- Consumer adapters can map the same method set to Timber, Crashlytics, platform logging, or another telemetry backend.

## Data & Control Flow

Consumers call methods on an implementation with Android `Bundle` data, `Throwable` instances, log priority, tags, messages, and identifiers. The interface itself stores no state and performs no side effects.

## Integration Points

- Depends on Android `Bundle` and `Log` types.
- Consumed by application or library code that wants a stable analytics boundary without taking a direct SDK dependency here.
