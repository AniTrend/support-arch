package io.wax911.support.model

import okhttp3.Headers
import okhttp3.ResponseBody

data class ModelWrapper<T>(val code: Int,
                           val model: T?,
                           val headers: Headers,
                           val error: ResponseBody?)