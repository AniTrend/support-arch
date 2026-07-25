# extension/src/main/kotlin/co/anitrend/arch/extension/settings/

## Responsibility

Provides observable typed setting implementations backed by shared preferences.

## Design Patterns

Each setting class extends `AbstractSetting<T>`, resolves its string key from resources, delegates value access to a typed preference delegate, and exposes a `callbackFlow` for preference changes.

## Data & Control Flow

When the matching shared preference key changes, the listener sends the current value to the flow. Closing the flow unregisters the listener.

## Integration Points

Uses delegates from `preference`, shared preferences from Android, resources for keys, and `settings/contract` for the base contract.
