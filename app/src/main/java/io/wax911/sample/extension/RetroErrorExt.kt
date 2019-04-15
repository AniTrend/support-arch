package io.wax911.sample.extension

import okhttp3.ResponseBody
import timber.log.Timber

/**
 * Retrofit error handler, this extension function will log the response error in json format
 * to the console using [Timber]
 */
fun ResponseBody?.logError() {
    Timber.d(this?.let {
        try {
            val json = it.string()
            if (json.isNotEmpty())
                json
            else
                "Unknown exception, error body is empty"
        } catch (e: Exception) {
            e.localizedMessage
        }
    } ?: "Response body is null")
}