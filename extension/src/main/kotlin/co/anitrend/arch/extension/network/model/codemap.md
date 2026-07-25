# extension/src/main/kotlin/co/anitrend/arch/extension/network/model/

## Responsibility

Defines connectivity state values.

## Design Patterns

`ConnectivityState` is a sealed class with `Unknown`, `Connected`, and `Disconnected` objects.

## Data & Control Flow

The connectivity monitor emits these values as Android network callbacks arrive.

## Integration Points

Used by `ISupportConnectivity` and `SupportConnectivity` consumers.
