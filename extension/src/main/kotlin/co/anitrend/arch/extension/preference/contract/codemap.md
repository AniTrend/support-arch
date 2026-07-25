# extension/src/main/kotlin/co/anitrend/arch/extension/preference/contract/

## Responsibility

Defines the base application preference contract.

## Design Patterns

`ISupportPreference` exposes common settings for new-installation status and version code as `AbstractSetting` values.

## Data & Control Flow

Concrete preference implementations provide the actual settings backed by shared preferences.

## Integration Points

Implemented by subclasses of `SupportPreference` and integrated with `settings.contract.AbstractSetting`.
