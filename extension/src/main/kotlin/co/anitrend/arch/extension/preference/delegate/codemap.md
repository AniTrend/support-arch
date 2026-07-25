# extension/src/main/kotlin/co/anitrend/arch/extension/preference/delegate/

## Responsibility

Defines the internal contract for typed settings property delegates.

## Design Patterns

`ISupportPreferenceDelegate<T>` extends `ReadWriteProperty<AbstractSetting<T>, T>` and requires a preference key plus default value.

## Data & Control Flow

Concrete delegates read from and write to `SharedPreferences` through their `AbstractSetting` receiver.

## Integration Points

Implemented by typed delegates in the parent `preference` package and used by `settings` classes.
