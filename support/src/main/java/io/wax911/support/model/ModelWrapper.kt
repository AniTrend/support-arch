package io.wax911.support.model

import okhttp3.Headers
import okhttp3.ResponseBody

data class ModelWrapper<T>(val code: Int = 400,
                           val model: T? = null,
                           val headers: Headers? = null,
                           val error: ResponseBody? = null)