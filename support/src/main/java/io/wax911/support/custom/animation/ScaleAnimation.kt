package io.wax911.support.custom.animation

import android.animation.Animator
import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.LinearInterpolator
import io.wax911.support.base.animation.SupportAnimation

/**
 * Created by max on 2018/02/24.
 */

class ScaleAnimation(private val from: Float = .85f,
                     private val to: Float = 1f) : SupportAnimation {

    private val linearInterpolator by lazy { LinearInterpolator() }

    override fun getAnimators(view: View): Array<Animator> {
        val scaleX = ObjectAnimator.ofFloat(view, "scaleX", from, to)
        val scaleY = ObjectAnimator.ofFloat(view, "scaleY", from, to)
        return arrayOf(scaleX, scaleY)
    }

    override fun getInterpolator() = linearInterpolator
}
