# core/src/main/kotlin/co/anitrend/arch/core/presenter/contract/

## Responsibility

Presenter contract exposing the settings dependency used by support presenters.

## Design Patterns

Small interface used as the stable boundary for presenter settings access.

## Data & Control Flow

Implementations provide a `SupportPreference`; callers read settings through the contract without knowing the concrete presenter type.

## Integration Points

Implemented by `SupportPresenter` and backed by extension `SupportPreference`.
