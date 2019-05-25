package io.wax911.sample.data.extension

import android.content.Context
import com.google.gson.reflect.TypeToken
import io.wax911.sample.data.api.RetroFactory
import java.lang.reflect.Type

inline fun <reified T> getTypeToken(): Type =
    object : TypeToken<T>() {}.type

@Deprecated("Use the koin `by inject()` varient")
inline fun <reified T> Context.getEndPointOf() : T =
    RetroFactory.newInstance(this)
        .createService(T::class.java)