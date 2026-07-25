# recycler/src/main/kotlin/co/anitrend/arch/recycler/observer/

## Responsibility

Adapter data observer proxy for offsetting notifications when extra views are present.

## Design Patterns

Proxy pattern around `RecyclerView.AdapterDataObserver`.

## Data & Control Flow

Forwarded adapter callbacks add `additionalViewAdapterViewCount` to start positions so observers stay aligned with headers or other inserted views.

## Integration Points

Useful with composed adapters or adapters with additional rows. Integrates with RecyclerView observer APIs.
