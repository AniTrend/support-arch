package io.wax911.support.core.animator.contract

import android.animation.Animator
import android.view.View
import android.view.animation.Interpolator

/**
 * Contract details for animators
 */
interface ISupportAnimator {

    val interpolator : Interpolator

    fun getAnimators(view: View): Array<Animator>

    @AnimatorDuration
    fun getAnimationDuration(): Int = AnimatorDuration.SHORT
}
