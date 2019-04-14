package io.wax911.support.core.wrapper

import okhttp3.Headers
import okhttp3.ResponseBody

data class ModelWrapper<T>(val code: Int? = null,
                           val model: T? = null,
                           val headers: Headers? = null,
                           val error: ResponseBody? = null)