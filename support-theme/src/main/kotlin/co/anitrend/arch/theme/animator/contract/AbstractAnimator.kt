package co.anitrend.arch.theme.animator.contract

import android.animation.Animator
import android.view.View
import android.view.animation.Interpolator

/**
 * Contract recycler animator
 *
 * @since v0.9.X
 */
abstract class AbstractAnimator {

    protected open val animationDuration: SupportAnimatorDuration =
        SupportAnimatorDuration.SHORT

    abstract val interpolator : Interpolator

    abstract fun getAnimators(view: View): Array<Animator>
}
