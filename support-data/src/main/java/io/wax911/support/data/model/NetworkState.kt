package io.wax911.support.data.model

import io.wax911.support.data.model.contract.SupportState
import io.wax911.support.data.model.contract.SupportStateContract

/**
 * UI informing state representing ongoing, completed or failed requests
 *
 * @param code The response code from the last request
 * @param status The UI state that one or more components should show
 * @param message Message to display if any are available
 *
 * @see [SupportStateContract] for a list of possible UI states
 */
data class NetworkState(
    val code: Int? = null,
    val status: SupportState,
    val message: String? = null
) {

    fun isLoaded() = status == SupportStateContract.CONTENT

    companion object {

        val LOADED = NetworkState(
            status = SupportStateContract.CONTENT
        )

        val LOADING = NetworkState(
            status = SupportStateContract.LOADING
        )

        fun error(msg: String?) = NetworkState(
            status = SupportStateContract.ERROR,
            message = msg
        )
    }
}