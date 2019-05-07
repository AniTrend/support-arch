package io.wax911.sample.core.extension

import android.content.Context
import com.google.gson.reflect.TypeToken
import io.wax911.sample.core.api.RetroFactory
import java.lang.reflect.Type

inline fun <reified T> getTypeToken(): Type =
    object : TypeToken<T>() {}.type

inline fun <reified T> Context.getEndPointOf() : T =
    RetroFactory.getInstance(this)
        .retrofit.create(T::class.java)