package io.wax911.support.model

import okhttp3.Headers
import okhttp3.ResponseBody
import java.lang.Exception

sealed class ModelWrapper<T>(val code: Int = 400,
                           val model: T? = null,
                           val headers: Headers? = null,
                           val error: ResponseBody? = null) {
    data class Success<out T: Any>(val data: T?, val headers: Headers?): Result<T>()
    data class Error<out T: ResponseBody>(val exception: Exception?): Result<T>()
}