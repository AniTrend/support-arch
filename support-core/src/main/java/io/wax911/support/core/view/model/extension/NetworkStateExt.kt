package io.wax911.support.core.view.model.extension

import io.wax911.support.core.view.model.NetworkState


fun NetworkState.isSuccessful() = code in 200..226
fun NetworkState.isRedirect() = code in 300..308

fun NetworkState.isBadRequest() = code == 400
fun NetworkState.isUnauthorized() = code == 401
fun NetworkState.isPaymentRequired() = code == 402
fun NetworkState.isForbidden() = code == 403
fun NetworkState.isNotFound() = code == 404
fun NetworkState.isRateLimitReached() = code == 429

fun NetworkState.isServerError() = code in 500..599
fun NetworkState.isRequestFailure() = code == null