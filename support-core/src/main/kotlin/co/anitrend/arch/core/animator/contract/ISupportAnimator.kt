package co.anitrend.arch.core.animator.contract

import android.animation.Animator
import android.view.View
import android.view.animation.Interpolator

/**
 * Contract details for animators
 *
 * @since v0.9.X
 */
interface ISupportAnimator {

    val interpolator : Interpolator

    fun getAnimators(view: View): Array<Animator>

    @SupportAnimatorDuration
    fun getAnimationDuration(): Int = SupportAnimatorDuration.SHORT
}
