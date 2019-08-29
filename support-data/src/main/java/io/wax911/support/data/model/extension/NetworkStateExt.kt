package io.wax911.support.data.model.extension

import io.wax911.support.data.model.NetworkState


fun NetworkState.isLoading() = this is NetworkState.Loading
fun NetworkState.isSuccess() = this is NetworkState.Success
fun NetworkState.isError() = this is NetworkState.Error


fun NetworkState.isRedirect() = this is NetworkState.Error && code in 300..308

fun NetworkState.isBadRequest() = this is NetworkState.Error && code == 400
fun NetworkState.isUnauthorized() = this is NetworkState.Error && code == 401
fun NetworkState.isPaymentRequired() = this is NetworkState.Error && code == 402
fun NetworkState.isForbidden() = this is NetworkState.Error && code == 403
fun NetworkState.isNotFound() = this is NetworkState.Error && code == 404
fun NetworkState.isRateLimitReached() = this is NetworkState.Error && code == 429

fun NetworkState.isServerError() = this is NetworkState.Error && code in 500..599
fun NetworkState.isWTF() = this is NetworkState.Error && code == null