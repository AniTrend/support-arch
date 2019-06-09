package io.wax911.support.data.model

import io.wax911.support.data.model.contract.SupportStateType

/**
 * UI informing state representing ongoing, completed or failed requests
 *
 * @param code The response code from the last request
 * @param status The UI state that one or more components should show
 * @param message Message to display if any are available
 *
 * @see [SupportStateType] for a list of possible UI states
 */
data class NetworkState(
    val code: Int? = null,
    @SupportStateType
    val status: Int,
    val message: String? = null
) {
    companion object {

        val LOADED = NetworkState(
            status = SupportStateType.CONTENT
        )

        val LOADING = NetworkState(
            status = SupportStateType.LOADING
        )

        fun error(msg: String?) = NetworkState(
            status = SupportStateType.ERROR,
            message = msg
        )
    }
}