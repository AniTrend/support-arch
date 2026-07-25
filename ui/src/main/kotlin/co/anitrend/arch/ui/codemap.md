# ui/src/main/kotlin/co/anitrend/arch/ui/

## Responsibility

Public UI package for reusable Android screen scaffolding and custom widgets.

## Design Patterns

Lifecycle controller pattern for activities and fragments, presenter pattern for list screens, custom view contract for widgets, and extension helpers for common widget setup.

## Data & Control Flow

View models expose `LoadState`; fragments observe it and delegate to presenters; presenters update adapters or state layout; state widgets render loading, error, or content and emit retry clicks.

## Integration Points

Integrates with core model contracts, domain `LoadState`, recycler adapters, theme resources, and extension lifecycle or coroutine helpers.
