# ui/src/main/kotlin/co/anitrend/arch/ui/fragment/

## Responsibility

Base fragment implementation for support screens.

## Design Patterns

Template fragment with optional menu and layout resource constructor parameters plus lifecycle controller and coroutine scope delegation.

## Data & Control Flow

On create, it retains the instance, initializes components, and enables menu inflation when requested. On create view, it inflates the configured layout. On view created, it asks subclasses to attach view model observers.

## Integration Points

Extended by `SupportFragmentList` and app fragments. Uses AndroidX Fragment APIs and `ILifecycleController`.
