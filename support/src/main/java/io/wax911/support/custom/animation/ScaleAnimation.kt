package io.wax911.support.custom.animation

import android.animation.Animator
import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.LinearInterpolator
import io.wax911.support.base.animation.AnimationBase
import io.wax911.support.base.animation.AnimationDuration

/**
 * Created by max on 2018/02/24.
 */

class ScaleAnimation : AnimationBase {

    private val FROM: Float
    private val TO: Float
    private val interpolator = LinearInterpolator()

    constructor() {
        this.FROM = .85f
        this.TO = 1f
    }

    constructor(FROM: Float, TO: Float) {
        this.FROM = FROM
        this.TO = TO
    }

    override fun getAnimators(view: View): Array<Animator> {
        val scaleX = ObjectAnimator.ofFloat(view, "scaleX", FROM, TO)
        val scaleY = ObjectAnimator.ofFloat(view, "scaleY", FROM, TO)
        return arrayOf(scaleX, scaleY)
    }

    override fun getInterpolator() = interpolator

    @AnimationDuration
    override fun getAnimationDuration() = AnimationDuration.SHORT

}
