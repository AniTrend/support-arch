# core/src/main/res/

## Responsibility

Core Android resources, currently FileProvider path metadata.

## Design Patterns

Minimal resource set reserved for framework integration rather than UI styling.

## Data & Control Flow

The XML path file is packaged into the module resources and referenced by a consuming app provider declaration.

## Integration Points

Pairs with `SupportFileProvider` in the provider package.
