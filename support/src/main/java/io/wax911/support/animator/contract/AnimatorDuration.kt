package io.wax911.support.animator.contract

import androidx.annotation.IntDef

@Retention(AnnotationRetention.SOURCE)
@IntDef(AnimatorDuration.SHORT, AnimatorDuration.MEDIUM, AnimatorDuration.LONG)
annotation class AnimatorDuration {
    companion object {
        const val SHORT = 250
        const val MEDIUM = 500
        const val LONG = 750
    }
}
