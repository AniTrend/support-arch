# core/src/main/kotlin/co/anitrend/arch/core/presenter/

## Responsibility

Base presenter abstraction for components that need context, settings, and lifecycle callbacks.

## Design Patterns

Abstract class combines `ISupportPresenter` with `SupportLifecycle` from extension.

## Data & Control Flow

Consumers subclass `SupportPresenter`, receive application context and a `SupportPreference` implementation, then participate in lifecycle attachment through extension helpers.

## Integration Points

Used by app or ui presenter layers. Depends on extension lifecycle and preference contracts.
