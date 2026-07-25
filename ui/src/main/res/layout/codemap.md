# ui/src/main/res/layout/

## Responsibility

State layout child views for full screen loading and error states.

## Design Patterns

One layout per non content state, each bound by generated ViewBinding in `SupportStateLayout`.

## Data & Control Flow

The state layout inflates these children into a ViewFlipper, updates text and images from configuration or load state, and wires retry clicks.

## Integration Points

Uses theme dimensions, Material widgets, and ui binding classes.
