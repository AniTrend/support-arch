# recycler/src/main/kotlin/co/anitrend/arch/recycler/shared/

## Responsibility

Shared recycler components for load state rows.

## Design Patterns

Adapter plus item model split. The adapter chooses a row type from `LoadState`; model classes bind individual XML layouts.

## Data & Control Flow

A `LoadState` enters `SupportLoadStateAdapter`, which inserts, removes, or refreshes one row. The row item binds loading, error, or default content and emits retry clicks for errors.

## Integration Points

Used as header and footer by `SupportAdapterController` and ui list fragments. Depends on core state config, domain load state, extension visibility helpers, and theme resources.
