# theme/src/main/kotlin/co/anitrend/arch/theme/animator/

## Responsibility

Concrete view animators shared by presentation modules.

## Design Patterns

Strategy implementation of `AbstractAnimator`; `ScaleAnimator` supplies paired `scaleX` and `scaleY` object animators with a linear interpolator.

## Data & Control Flow

Adapters ask an animator for view specific `Animator` instances, then apply duration and interpolator before starting them.

## Integration Points

Used by recycler adapters through `ISupportAdapter.customSupportAnimator`; depends only on Android animation APIs and the animator contract package.
