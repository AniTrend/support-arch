# extension/src/main/kotlin/co/anitrend/arch/extension/ext/

## Responsibility

Collects Android and Kotlin extension functions for activities, fragments, contexts, menus, drawables, views, strings, collections, dimensions, and lazy argument access.

## Design Patterns

Uses extension functions, reified generics, lazy delegates, AndroidX compatibility helpers, and callback flows for broadcasts.

## Data & Control Flow

Callers invoke helpers directly on receiver types. Some helpers query system services, start or stop services, schedule alarms, modify window insets, mutate views, or convert resources.

## Integration Points

Used across UI and app layers. Depends on AndroidX Core, Fragment, Material Snackbar, and Timber logging.
