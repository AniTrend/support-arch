package io.wax911.support.core.view.model.contract

import androidx.annotation.IntDef

@IntDef(
    SupportStateType.LOADING,
    SupportStateType.CONTENT,
    SupportStateType.ERROR
)
@Retention(AnnotationRetention.SOURCE)
annotation class SupportStateType {
    companion object {
        const val LOADING = 0
        const val CONTENT = 1
        const val ERROR = 2
    }
}