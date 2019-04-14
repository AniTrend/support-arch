package io.wax911.support.extension

import okhttp3.ResponseBody

/**
 * Retrofit error handler, this extension function will log the response error in json format
 * to the console
 */
fun ResponseBody?.logError(): String {
    return this?.let {
        try {
            val json = it.string()
            if (!json.isEmpty())
                json
            else
                "Unknown exception, error body is empty"
        } catch (e: Exception) {
            e.localizedMessage
        }
    } ?: "Response body is null"
}
