# buildSrc/src/main/java/co/anitrend/arch/buildSrc/plugin/

## Responsibility

Contains `CorePlugin`, the `co.anitrend.arch` plugin implementation applied by library modules.

## Design Patterns

- Orchestrator plugin delegates each concern to extension functions in `components`.
- Module classification helpers in `extensions` decide JVM versus Android behavior.

## Data & Control Flow

`apply` first calls `configurePlugins`, then skips Android configuration for annotation and processor modules, then configures Dokka, source artifacts, dependencies, Spotless, and diagnostic logging.

## Integration Points

- Plugin marker resource points at `CorePlugin`.
- Calls component functions in `plugin/components`.
- Uses project helpers from `plugin/extensions`.
