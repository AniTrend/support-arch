# extension/src/main/kotlin/co/anitrend/arch/extension/

## Responsibility

Package root for reusable Android and Kotlin extension utilities.

## Design Patterns

Organizes opt-in annotations, coroutine scope factories, dispatchers, Android extension functions, startup initialization, lifecycle logging hooks, connectivity flows, settings, preferences, and date or paging helpers.

## Data & Control Flow

Consumers call extension functions directly, create helper objects, or collect flows from connectivity and settings wrappers. Internal delegates and startup code support these public APIs.

## Integration Points

Consumed by multiple modules for dispatchers and utility helpers. The manifest in `src/main` wires the ThreeTen initializer for date handling.
