# extension/src/main/kotlin/co/anitrend/arch/extension/network/

## Responsibility

Provides connectivity monitoring utilities.

## Design Patterns

`SupportConnectivity` implements `ISupportConnectivity` using `ConnectivityManager`, network capabilities, and a `callbackFlow` over `NetworkCallback` events.

## Data & Control Flow

`isConnected` snapshots currently available networks. `connectivityStateFlow` emits connected, disconnected, or unknown states while registered and unregisters callbacks when closed.

## Integration Points

Requires `ACCESS_NETWORK_STATE` from the module manifest. Uses `network/model` for state values and `network/contract` for the public API.
