# analytics/src/

## Responsibility

Groups analytics source sets. Only `main` contains production code in this scope.

## Design Patterns

Source-set boundary used by the Android Gradle plugin.

## Data & Control Flow

Gradle reads `main` sources when compiling the analytics artifact.

## Integration Points

See `main/codemap.md` for the Android manifest and Kotlin package map.
