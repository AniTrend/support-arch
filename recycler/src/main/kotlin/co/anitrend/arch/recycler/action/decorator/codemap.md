# recycler/src/main/kotlin/co/anitrend/arch/recycler/action/decorator/

## Responsibility

Visual selection decoration contract.

## Design Patterns

Default method implementation pattern. The decorator can be used as is or overridden per item.

## Data & Control Flow

When an item is selected, check boxes are checked or a selection frame drawable is applied. When deselected, those visuals are cleared.

## Integration Points

Uses `co.anitrend.arch.theme.R.drawable.selection_frame`; called by `SupportViewHolder`.
