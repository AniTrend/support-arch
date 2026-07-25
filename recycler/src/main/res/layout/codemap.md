# recycler/src/main/res/layout/

## Responsibility

XML layouts for loading, error, and default list state rows.

## Design Patterns

One layout per load state category. IDs map directly to generated binding classes used by shared item models.

## Data & Control Flow

Loading shows a small progress bar plus optional text. Error shows a message and retry action. Default shows optional single line text and can be hidden by the model.

## Integration Points

Bound by `SupportLoadingItem`, `SupportErrorItem`, and `SupportDefaultItem`.
