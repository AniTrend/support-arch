# theme/src/main/kotlin/co/anitrend/arch/theme/animator/contract/

## Responsibility

Animation contracts and shared duration constants.

## Design Patterns

Abstract base class requires an interpolator and animator factory; enum maps semantic durations to millisecond runtime values.

## Data & Control Flow

Concrete animators extend `AbstractAnimator`, optionally override `animationDuration`, and return animators for each bound item view.

## Integration Points

Consumed by `theme/animator` and recycler adapter binding. Keeps animation configuration reusable without depending on RecyclerView.
