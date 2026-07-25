# core/src/main/kotlin/co/anitrend/arch/core/provider/

## Responsibility

FileProvider subclass and companion helper for safe content URI creation.

## Design Patterns

Framework wrapper pattern. `SupportFileProvider` extends AndroidX `FileProvider` and exposes a static style `uriForFile` proxy.

## Data & Control Flow

Callers pass context, provider authority, and file; the helper delegates to `FileProvider.getUriForFile` and returns a `content://` URI.

## Integration Points

Requires a matching provider declaration in a consuming app manifest and path XML under `core/src/main/res/xml`.
