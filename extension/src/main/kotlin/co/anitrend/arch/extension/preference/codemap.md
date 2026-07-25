# extension/src/main/kotlin/co/anitrend/arch/extension/preference/

## Responsibility

Provides shared preference wrappers and internal property delegates for settings.

## Design Patterns

`SupportPreference` delegates `SharedPreferences` operations and exposes application settings through `ISupportPreference`. Internal delegates implement typed read and write behavior for enum, primitive, string, nullable string, and string set values.

## Data & Control Flow

Setting classes access `SharedPreferences` through delegates. Reads return persisted values or defaults; writes use AndroidX `edit` helpers.

## Integration Points

Used by `extension.settings` classes. `SupportPreference` defaults to `PreferenceManager.getDefaultSharedPreferences(context)`.
