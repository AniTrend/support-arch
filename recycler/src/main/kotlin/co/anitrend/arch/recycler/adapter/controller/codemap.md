# recycler/src/main/kotlin/co/anitrend/arch/recycler/adapter/controller/

## Responsibility

Concrete controller that manages adapter load state listeners and ConcatAdapter header or footer composition.

## Design Patterns

Controller pattern around `LoadStateManager`; stores listeners in `CopyOnWriteArrayList` and builds ConcatAdapter combinations.

## Data & Control Flow

`setLoadState` reaches the controller, which updates `LoadStateManager`; listeners update header and footer `SupportLoadStateAdapter` instances based on top or bottom load positions.

## Integration Points

Used by `SupportAdapter` and `SupportListAdapter`; consumes domain `LoadState` and recycler shared load state adapters.
