# core/src/main/kotlin/co/anitrend/arch/core/model/

## Responsibility

Shared model level contracts for view model state and state layout configuration.

## Design Patterns

Interface contracts keep UI modules independent from concrete app view models and concrete state layout config classes.

## Data & Control Flow

View models expose `LiveData<R>` plus `LiveData<LoadState>` and suspend retry or refresh hooks. State layout config supplies optional drawables, messages, actions, and animations.

## Integration Points

Consumed by ui `SupportStateLayout`, recycler `SupportLoadStateAdapter`, and list fragments. Depends on domain `LoadState` and Android resource annotations.
