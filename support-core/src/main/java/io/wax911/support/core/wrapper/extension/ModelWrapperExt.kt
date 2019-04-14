package io.wax911.support.core.wrapper.extension

import io.wax911.support.core.wrapper.ModelWrapper

fun ModelWrapper<*>.isSuccessful() = code in 200..226
fun ModelWrapper<*>.isRedirect() = code in 300..308

fun ModelWrapper<*>.isBadRequest() = code == 400
fun ModelWrapper<*>.isUnauthorized() = code == 401
fun ModelWrapper<*>.isPaymentRequired() = code == 402
fun ModelWrapper<*>.isForbidden() = code == 403
fun ModelWrapper<*>.isNotFound() = code == 404

fun ModelWrapper<*>.isServerError() = code in 500..599
fun ModelWrapper<*>.isRequestFailure() = code == null