package io.wax911.support.custom.worker

import io.wax911.support.model.ModelWrapper
import retrofit2.Call

abstract class SupportRequestClient {

    abstract fun <T> executeUsing(call: Call<T>) : ModelWrapper<T?>
}
