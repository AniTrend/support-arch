# domain/

## Responsibility

Low-level Android library module for shared domain value types. It defines operation load states, request errors, and UI state contracts without depending on higher-level modules.

## Design Patterns

Uses sealed classes for finite states, data classes for immutable error and state payloads, and abstract contracts for UI state behavior.

## Data & Control Flow

Consumers create or observe `LoadState`, `NetworkState`, `RequestError`, and `UiState` values. The module does not execute workflows, it supplies the types that other modules pass through flows, callbacks, and presenters.

## Integration Points

Builds with the shared `co.anitrend.arch` Gradle plugin and namespace `co.anitrend.arch.domain`. `request` depends on it for request errors and load states, while `data` depends on it for `UiState` and `LoadState`.
