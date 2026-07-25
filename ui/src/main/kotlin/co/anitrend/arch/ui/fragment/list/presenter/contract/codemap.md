# ui/src/main/kotlin/co/anitrend/arch/ui/fragment/list/presenter/contract/

## Responsibility

Contract for list presenters.

## Design Patterns

Lifecycle aware presenter interface with setup, load state, and reset hooks.

## Data & Control Flow

Fragments call `onCreateView`, forward observed `LoadState` through `onNetworkObserverChanged`, and ask presenters to reset widget states after model updates.

## Integration Points

Implemented by `SupportListPresenter`; uses domain `LoadState`, extension `SupportLifecycle`, and state layout contract types.
