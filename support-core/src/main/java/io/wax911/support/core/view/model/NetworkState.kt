package io.wax911.support.core.view.model

import io.wax911.support.core.view.model.contract.SupportStateType


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