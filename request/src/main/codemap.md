# request/src/main/

## Responsibility

Main source set for request lifecycle code and Android packaging metadata.

## Design Patterns

Keeps the manifest separate from Kotlin request orchestration packages.

## Data & Control Flow

Runtime control flow lives under `kotlin/co/anitrend/arch/request`; the manifest only identifies the Android library source set.

## Integration Points

Integrated by Gradle as the `main` source set for `:request`.
