# ui/src/main/kotlin/co/anitrend/arch/ui/view/widget/model/

## Responsibility

Concrete state layout configuration model.

## Design Patterns

Data class implementation of core `IStateLayoutConfig`.

## Data & Control Flow

Consumers construct it with optional drawable, message, retry, and animation resource ids. State layout and recycler load state rows read the config when binding UI.

## Integration Points

Used by `ISupportFragmentList`, `SupportStateLayout`, and recycler support load state items.
