package co.anitrend.arch.core.animator.contract

import android.animation.Animator
import android.view.View
import android.view.animation.Interpolator

/**
 * Contract details for animators
 */
interface ISupportAnimator {

    val interpolator : Interpolator

    fun getAnimators(view: View): Array<Animator>

    @SupportAnimatorDuration
    fun getAnimationDuration(): Int = SupportAnimatorDuration.SHORT
}
