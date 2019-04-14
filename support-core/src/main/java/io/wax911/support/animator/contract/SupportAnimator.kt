package io.wax911.support.animator.contract

import android.animation.Animator
import android.view.View
import android.view.animation.Interpolator

interface SupportAnimator {

    fun getAnimators(view: View): Array<Animator>

    fun getInterpolator(): Interpolator

    @AnimatorDuration
    fun getAnimationDuration(): Int = AnimatorDuration.SHORT
}
