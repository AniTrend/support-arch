# core/src/main/

## Responsibility

Main source set for core Kotlin abstractions plus FileProvider XML paths.

## Design Patterns

Low level Android support contracts with no UI resources other than provider path configuration.

## Data & Control Flow

Consumers subclass workers and presenters, implement state contracts, or call the file provider helper. Resource XML is packaged for FileProvider path metadata.

## Integration Points

Depends on extension, data, domain, Material Components, and WorkManager. Used by recycler and ui for state configuration and lifecycle presentation helpers.
