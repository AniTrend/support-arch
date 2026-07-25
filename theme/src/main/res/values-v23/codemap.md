# theme/src/main/res/values-v23/

## Responsibility

API 23 system bar overrides for `SupportTheme.AppTheme` and translucent windows.

## Design Patterns

Version qualified resources enable light status bar flags and transparent system bars only where supported.

## Data & Control Flow

Android resolves these styles on API 23 and later; callers apply the theme and receive system bar configuration from XML.

## Integration Points

Integrates with app activities that inherit `SupportTheme.AppTheme` or `SupportTheme.Translucent`.
