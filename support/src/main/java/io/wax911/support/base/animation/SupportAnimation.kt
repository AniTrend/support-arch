package io.wax911.support.base.animation

import android.animation.Animator
import android.view.View
import android.view.animation.Interpolator

interface SupportAnimation {

    fun getAnimators(view: View): Array<Animator>

    fun getInterpolator(): Interpolator

    fun getAnimationDuration(): Int
}
