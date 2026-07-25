# ui/src/main/kotlin/co/anitrend/arch/ui/view/widget/contract/

## Responsibility

State layout contract for load state driven widgets.

## Design Patterns

Custom view contract plus observable flow properties and child index constants.

## Data & Control Flow

Presenters write to `loadStateFlow` and `stateConfigFlow`; listeners observe `interactionFlow` for retry actions. Boolean properties expose the currently displayed child.

## Integration Points

Implemented by `SupportStateLayout` and consumed by `SupportListPresenter`.
