# extension/src/main/kotlin/co/anitrend/arch/extension/network/contract/

## Responsibility

Defines the connectivity monitoring contract.

## Design Patterns

`ISupportConnectivity` exposes a boolean connection snapshot and a `Flow<ConnectivityState>` for changes.

## Data & Control Flow

Consumers can check immediate connectivity or collect state changes over time.

## Integration Points

Implemented by `SupportConnectivity` and depends on `network/model` state values.
