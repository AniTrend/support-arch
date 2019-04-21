package io.wax911.support.core.wrapper.extension

import io.wax911.support.core.wrapper.RetroWrapper

fun RetroWrapper<*>.isSuccessful() = code in 200..226
fun RetroWrapper<*>.isRedirect() = code in 300..308

fun RetroWrapper<*>.isBadRequest() = code == 400
fun RetroWrapper<*>.isUnauthorized() = code == 401
fun RetroWrapper<*>.isPaymentRequired() = code == 402
fun RetroWrapper<*>.isForbidden() = code == 403
fun RetroWrapper<*>.isNotFound() = code == 404
fun RetroWrapper<*>.isRateLimitReached() = code == 429

fun RetroWrapper<*>.isServerError() = code in 500..599
fun RetroWrapper<*>.isRequestFailure() = code == null