# domain/src/main/

## Responsibility

Main source set for the domain module, containing the manifest and Kotlin package tree.

## Design Patterns

Separates Android packaging metadata from Kotlin contracts and value objects.

## Data & Control Flow

The manifest contributes only module packaging. Runtime-facing declarations live under `kotlin/co/anitrend/arch/domain`.

## Integration Points

Integrated by Gradle as the `main` Android source set for `:domain`.
