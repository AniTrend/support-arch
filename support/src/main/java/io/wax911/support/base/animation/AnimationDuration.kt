package io.wax911.support.base.animation

import androidx.annotation.IntDef

@Retention(AnnotationRetention.SOURCE)
@IntDef(AnimationDuration.SHORT, AnimationDuration.MEDIUM, AnimationDuration.LONG)
annotation class AnimationDuration {
    companion object {
        const val SHORT = 250
        const val MEDIUM = 500
        const val LONG = 750
    }
}
