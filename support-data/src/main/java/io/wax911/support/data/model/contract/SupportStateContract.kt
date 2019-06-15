package io.wax911.support.data.model.contract

import androidx.annotation.IntDef

@IntDef(
    SupportStateContract.LOADING,
    SupportStateContract.CONTENT,
    SupportStateContract.ERROR
)
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.TYPEALIAS, AnnotationTarget.PROPERTY)
annotation class SupportStateContract {
    companion object {
        const val LOADING = 0
        const val CONTENT = 1
        const val ERROR = 2
    }
}

/**
 * Represents states denominated by:
 *
 * LOADING = 0
 * CONTENT = 1
 * ERROR = 2
 */
@SupportStateContract
typealias SupportState = Int