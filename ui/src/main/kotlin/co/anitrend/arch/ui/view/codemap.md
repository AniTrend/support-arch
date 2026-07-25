# ui/src/main/kotlin/co/anitrend/arch/ui/view/

## Responsibility

Custom view package for reusable image and state widgets plus view contracts.

## Design Patterns

Contract first widget design. Custom views implement `CustomView` for constructor initialization and recycle cleanup.

## Data & Control Flow

Widgets read XML attributes during `onInit`, update measured size or child state, and release listeners or coroutine work when detached.

## Integration Points

Uses AndroidX widgets, domain load state, recycler click models, and ui resource attributes and layouts.
