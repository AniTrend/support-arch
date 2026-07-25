# ui/src/main/kotlin/co/anitrend/arch/ui/view/image/

## Responsibility

Aspect ratio aware image view.

## Design Patterns

Custom view extending `AppCompatImageView` and implementing `CustomView`.

## Data & Control Flow

The view reads `aspectRatio` from XML attributes, adjusts measured height from width or width from height, and clears click listeners on detach.

## Integration Points

Uses ui `SupportImageView` styleable attributes. Useful for fixed ratio image cells in recycler or pager screens.
