# core/src/main/kotlin/co/anitrend/arch/core/

## Responsibility

Core package grouping reusable model contracts, presenter base classes, file provider helper, and WorkManager worker bases.

## Design Patterns

Abstract base classes and small interfaces separate consumer implementation from Android framework glue.

## Data & Control Flow

View models expose model and load state through `ISupportViewModelState`; presenters hold app settings and lifecycle behavior; workers delegate work to subclass overrides.

## Integration Points

Bridges domain `LoadState`, extension lifecycle and preference contracts, AndroidX WorkManager, and AndroidX FileProvider.
