package io.wax911.sample.extension

import android.content.Context
import com.google.gson.reflect.TypeToken
import io.wax911.sample.App
import io.wax911.sample.api.RetroFactory
import io.wax911.sample.dao.DatabaseHelper
import io.wax911.support.analytic.contract.ISupportAnalytics
import java.lang.reflect.Type

/**
 * Gets the application database
 */
fun Context.getDatabase() : DatabaseHelper =
        DatabaseHelper.getInstance(this)

inline fun <reified T> getTypeToken(): Type =
        object : TypeToken<T>() {}.type

inline fun <reified T> Context.getEndPointOf() : T =
        RetroFactory.getInstance(this)
                .retrofit.create(T::class.java)

fun Context?.getAnalytics(): ISupportAnalytics? = this?.let {
    val application = it.applicationContext as App
    application.analyticsUtil
}