package co.anitrend.arch.core.animator.contract

import androidx.annotation.IntDef

@Retention(AnnotationRetention.SOURCE)
@IntDef(SupportAnimatorDuration.SHORT, SupportAnimatorDuration.MEDIUM, SupportAnimatorDuration.LONG)
annotation class SupportAnimatorDuration {
    companion object {
        const val SHORT = 250
        const val MEDIUM = 500
        const val LONG = 750
    }
}
