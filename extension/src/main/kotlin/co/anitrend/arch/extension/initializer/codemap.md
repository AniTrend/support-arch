# extension/src/main/kotlin/co/anitrend/arch/extension/initializer/

## Responsibility

Contains AndroidX Startup initialization for ThreeTenABP.

## Design Patterns

`ThreeTenInitializer` implements `Initializer<Unit>` and calls `AndroidThreeTen.init(context)` with no dependencies.

## Data & Control Flow

AndroidX Startup invokes the initializer at app startup based on manifest metadata.

## Integration Points

Registered in `extension/src/main/AndroidManifest.xml`; supports date helpers that use ThreeTen backport types.
