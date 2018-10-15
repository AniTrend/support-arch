package io.wax911.support.custom.worker

import android.content.Context
import io.wax911.support.model.ModelWrapper
import kotlinx.coroutines.experimental.Deferred
import retrofit2.Call

abstract class SupportRequestClient {

    abstract suspend fun <T> executeUsing(call: Call<T>, context: Context) : ModelWrapper<T?>
}
