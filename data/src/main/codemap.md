# data/src/main/

## Responsibility

Main source set for the data module, containing manifest metadata and Kotlin data abstractions.

## Design Patterns

Keeps Android packaging separate from reusable repository-side Kotlin APIs.

## Data & Control Flow

Runtime flow is implemented under `kotlin/co/anitrend/arch/data`; the manifest only contributes module packaging.

## Integration Points

Integrated by Gradle as the `main` source set for `:data`.
