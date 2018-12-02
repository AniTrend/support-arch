package io.wax911.support.extension

import android.util.Log
import okhttp3.ResponseBody

/**
 * Retrofit error handler, this extension function will log the response error in json format
 * to the console
 */
fun ResponseBody?.logError() {
    this?.also {
        try {
            val json = it.string()
            if (!json.isEmpty()) Log.e("RetroErrorExt#logError", json)
            else Log.e("RetroErrorExt#logError", "Unknown exception, error body is empty")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
