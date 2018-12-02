package io.wax911.support.custom.animation

import android.animation.Animator
import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.LinearInterpolator
import io.wax911.support.base.animation.SupportAnimation
import io.wax911.support.base.animation.AnimationDuration

/**
 * Created by max on 2018/02/24.
 */

class ScaleAnimation : SupportAnimation {

    private val _from: Float
    private val _to: Float
    private val interpolator = LinearInterpolator()

    constructor() {
        this._from = .85f
        this._to = 1f
    }

    constructor(FROM: Float, TO: Float) {
        this._from = FROM
        this._to = TO
    }

    override fun getAnimators(view: View): Array<Animator> {
        val scaleX = ObjectAnimator.ofFloat(view, "scaleX", _from, _to)
        val scaleY = ObjectAnimator.ofFloat(view, "scaleY", _from, _to)
        return arrayOf(scaleX, scaleY)
    }

    override fun getInterpolator() = interpolator

    @AnimationDuration
    override fun getAnimationDuration() = AnimationDuration.SHORT

}
