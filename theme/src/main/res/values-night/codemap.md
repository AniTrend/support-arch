# theme/src/main/res/values-night/

## Responsibility

Dark mode overrides for theme colors, state booleans, and Material theme parents.

## Design Patterns

Qualifier based override set mirroring base resources while changing palettes and dark dialog parents.

## Data & Control Flow

When night mode is active Android resolves these resources, so `isEnvironmentNightMode` and theme attributes reflect dark values.

## Integration Points

Used automatically by Android resource resolution for apps using theme resources from this module.
