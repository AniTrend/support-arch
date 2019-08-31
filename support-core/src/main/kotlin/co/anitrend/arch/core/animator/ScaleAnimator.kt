package co.anitrend.arch.core.animator

import android.animation.Animator
import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.LinearInterpolator
import co.anitrend.arch.core.animator.contract.ISupportAnimator

/**
 * Created by max on 2018/02/24.
 *
 * Scale animator for recycler animations
 */

class ScaleAnimator(
    private val from: Float = .85f,
    private val to: Float = 1f
) : ISupportAnimator {

    override val interpolator = LinearInterpolator()

    override fun getAnimators(view: View): Array<Animator> {
        val scaleX = ObjectAnimator.ofFloat(view, "scaleX", from, to)
        val scaleY = ObjectAnimator.ofFloat(view, "scaleY", from, to)
        return arrayOf(scaleX, scaleY)
    }
}
