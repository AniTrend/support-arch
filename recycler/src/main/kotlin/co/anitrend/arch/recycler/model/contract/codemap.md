# recycler/src/main/kotlin/co/anitrend/arch/recycler/model/contract/

## Responsibility

Contracts for bindable recycler items and span sizing.

## Design Patterns

Item contract plus span contract. Binding, unbinding, selection decoration, and layout span sizing are owned by the item model.

## Data & Control Flow

During adapter binding, a mapped `IRecyclerItem` receives the Android view, payloads, click flow, and optional selection mode. During layout, the item reports its preferred span size.

## Integration Points

Implemented by app item models and built in support state items. Consumed by adapters and view holders.
