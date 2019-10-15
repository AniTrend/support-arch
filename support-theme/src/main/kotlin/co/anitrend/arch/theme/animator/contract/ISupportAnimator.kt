package co.anitrend.arch.theme.animator.contract

import android.animation.Animator
import android.view.View
import android.view.animation.Interpolator

/**
 * Contract recycler animator
 *
 * @since v0.9.X
 */
interface ISupportAnimator {

    val interpolator : Interpolator

    fun getAnimators(view: View): Array<Animator>

    fun getAnimationDuration(): SupportAnimatorDuration = SupportAnimatorDuration.SHORT
}
