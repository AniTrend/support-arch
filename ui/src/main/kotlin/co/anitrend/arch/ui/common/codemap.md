# ui/src/main/kotlin/co/anitrend/arch/ui/common/

## Responsibility

Lifecycle controller contract shared by support activities and fragments.

## Design Patterns

Small interface boundary for component initialization and optional view model state access.

## Data & Control Flow

Framework classes call `initializeComponents` at their setup point. List fragments call `viewModelState` for model, load state, retry, and refresh behavior.

## Integration Points

Implemented by `SupportActivity` and `SupportFragment`. Depends on core `ISupportViewModelState`.
