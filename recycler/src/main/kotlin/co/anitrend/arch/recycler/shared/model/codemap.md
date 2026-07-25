# recycler/src/main/kotlin/co/anitrend/arch/recycler/shared/model/

## Responsibility

Built in recycler item models for loading, error, and default load state rows.

## Design Patterns

Each class extends `RecyclerItem`, binds a generated ViewBinding layout, and uses `IStateLayoutConfig` for optional text and action resources.

## Data & Control Flow

Loading rows show optional loading text. Error rows show error message and optional retry button that emits `ClickableItem.State`. Default rows show optional success or empty message.

## Integration Points

Used only by `SupportLoadStateAdapter`; layouts live under `recycler/src/main/res/layout`.
