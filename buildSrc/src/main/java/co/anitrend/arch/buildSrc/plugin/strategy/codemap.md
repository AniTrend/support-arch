# buildSrc/src/main/java/co/anitrend/arch/buildSrc/plugin/strategy/

## Responsibility

Contains `DependencyStrategy`, the shared dependency policy for modules that should receive library defaults.

## Design Patterns

- Strategy object scoped to one Gradle `Project`.
- Dependency groups split default, lifecycle, logging, test, and coroutine concerns.

## Data & Control Flow

`ProjectDependencies.configureDependencies` creates the strategy and passes the module dependency handler. The strategy always adds Kotlin stdlib, Kotlin reflect, and unit test libraries. For modules allowed by `hasDependencies`, it also adds lifecycle, Timber, Android test, and coroutine dependencies.

## Integration Points

- Uses dependency aliases from `gradle/libs.versions.toml` through `Project.libs`.
- Uses dependency helper functions from `plugin/extensions`.
- Skips Android test dependencies for Kotlin JVM modules.
