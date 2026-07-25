# spotless/

## Responsibility

Contains the shared Kotlin license header template used by Spotless formatting.

## Design Patterns

Single header template with a `$YEAR` placeholder, applied consistently from the buildSrc Spotless convention.

## Data & Control Flow

`ProjectSpotless.configureSpotless` points Spotless at `spotless/copyright.kt`. During Spotless checks or applies, Kotlin files outside excluded build and test paths are checked against this header.

## Integration Points

- Consumed by `buildSrc/src/main/java/co/anitrend/arch/buildSrc/plugin/components/ProjectSpotless.kt`.
- Used by modules that apply the shared `co.anitrend.arch` plugin.
