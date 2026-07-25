# recycler/src/main/kotlin/co/anitrend/arch/recycler/

## Responsibility

Top level recycler package containing the lifecycle aware `SupportRecyclerView` plus adapter, holder, model, state, selection, and helper packages.

## Design Patterns

Lifecycle aware view wrapper and adapter composition. Most behavior is delegated into contracts and child packages.

## Data & Control Flow

`SupportRecyclerView` gates duplicate scroll listeners and clears its adapter on destroy when lifecycle attachment is used. Child packages handle item mapping, binding, load state, and interaction events.

## Integration Points

Consumes extension lifecycle helpers and Timber. Used directly by ui list presenters and recycler setup extensions.
