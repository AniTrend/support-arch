# ui/src/main/res/

## Responsibility

UI resources for custom widget attributes, state layout strings, and state layout XML.

## Design Patterns

Resource package supporting generated styleables and view binding.

## Data & Control Flow

Custom views read attributes from values resources. `SupportStateLayout` inflates loading and error layouts and binds their generated binding classes.

## Integration Points

Consumed by ui Kotlin widgets and by apps using `SupportImageView` or `SupportStateLayout` in XML.
