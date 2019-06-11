package io.wax911.sample.data.extension

import com.google.gson.reflect.TypeToken
import io.wax911.support.data.factory.contract.IRetrofitFactory
import java.lang.reflect.Type

inline fun <reified T> getTypeToken(): Type =
    object : TypeToken<T>() {}.type