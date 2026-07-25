# theme/src/main/res/drawable/

## Responsibility

Reusable shape drawables for dialog backgrounds and selection indication.

## Design Patterns

XML shape resources using theme attributes and shared dimensions.

## Data & Control Flow

Views and decorators reference these drawables by resource id; Android resolves colors from the active theme.

## Integration Points

Used by `ISelectionDecorator` in recycler and dialog styles in the theme resource set.
