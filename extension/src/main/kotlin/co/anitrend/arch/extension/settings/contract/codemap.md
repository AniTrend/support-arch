# extension/src/main/kotlin/co/anitrend/arch/extension/settings/contract/

## Responsibility

Defines the abstract setting model shared by typed settings.

## Design Patterns

`AbstractSetting<T>` holds an internal `SharedPreferences` instance and default value, requires an identifier, mutable value, and observable flow.

## Data & Control Flow

Typed subclasses map identifier changes to persisted value reads, writes, and flow emissions.

## Integration Points

Extended by setting classes in `extension.settings` and referenced by preference contracts and delegates.
