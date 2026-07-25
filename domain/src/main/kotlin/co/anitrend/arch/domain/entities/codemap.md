# domain/src/main/kotlin/co/anitrend/arch/domain/entities/

## Responsibility

Defines shared value types for load lifecycle and request failures.

## Design Patterns

`LoadState` is a sealed class with position-aware `Idle`, `Success`, `Loading`, and `Error` states. `NetworkState` is the deprecated older sealed state model. `RequestError` wraps topic, description, and throwable data as a `Throwable`.

## Data & Control Flow

Request and data layers emit or carry these objects as operation status changes occur. UI layers can switch on the sealed states and inspect positions for top, bottom, or undefined loading indicators.

## Integration Points

`request` maps request reports into `LoadState`. `data` exposes `Flow<LoadState>` through data sources and `DataState`. External apps can construct `RequestError` when reporting failures.
