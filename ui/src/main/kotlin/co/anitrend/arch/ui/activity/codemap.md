# ui/src/main/kotlin/co/anitrend/arch/ui/activity/

## Responsibility

Base activity implementation for support screens.

## Design Patterns

Template method pattern around `AppCompatActivity`: subclasses configure activity before `super.onCreate` and initialize components after post create.

## Data & Control Flow

On create, `configureActivity` runs before framework setup. `initializeComponents` runs in `onPostCreate`. Toolbar setup enables home as up and home presses route through the back dispatcher.

## Integration Points

Implements `ILifecycleController` and `CoroutineScope` via `MainScope`. Used by app activities that consume this architecture.
