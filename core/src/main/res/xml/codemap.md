# core/src/main/res/xml/

## Responsibility

FileProvider path XML for shared file URI permissions.

## Design Patterns

AndroidX FileProvider metadata format.

## Data & Control Flow

When a provider uses this XML as metadata, FileProvider validates requested files against the declared path roots.

## Integration Points

Consumed with `SupportFileProvider.uriForFile` and a matching app manifest provider authority.
