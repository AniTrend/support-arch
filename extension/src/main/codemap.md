# extension/src/main/

## Responsibility

Main source set for extension utilities and Android manifest declarations.

## Design Patterns

Combines Kotlin utility packages with manifest wiring for AndroidX Startup.

## Data & Control Flow

The manifest registers `ThreeTenInitializer`; runtime helper code lives under `kotlin/co/anitrend/arch/extension`.

## Integration Points

Integrated by Gradle as the `main` source set for `:extension`.
