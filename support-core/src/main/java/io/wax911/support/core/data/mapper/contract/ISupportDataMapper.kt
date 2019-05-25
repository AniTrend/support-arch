package io.wax911.support.core.data.mapper.contract

import androidx.room.RoomDatabase
import io.wax911.support.core.util.SupportCoroutineHelper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.core.KoinComponent
import retrofit2.Callback
import retrofit2.Response

interface ISupportDataMapper<S, D> : KoinComponent, SupportCoroutineHelper {

    val database: RoomDatabase

    val responseCallback: Callback<S>

    val TAG
        get() = javaClass.simpleName

    /**
     * Created mapped objects and handles the database operations which may be required to map various objects,
     * called in [retrofit2.Callback.onResponse] after assuring that the response was a success
     * @see [responseCallback]
     *
     * @param response retrofit response containing data
     * @return Mapped object that will be consumed by [onResponseDatabaseInsert]
     */
    suspend fun onResponseMapFrom(response: Response<S?>): D

    /**
     * Inserts the given object into the implemented room database,
     * called in [retrofit2.Callback.onResponse]
     * @see [responseCallback]
     *
     * @param mappedData mapped object from [onResponseMapFrom] to insert into the database
     */
    suspend fun onResponseDatabaseInsert(mappedData: D)

    /**
     * Coroutine dispatcher specification
     *
     * @return [kotlinx.coroutines.Dispatchers.Default] by default
     */
    override val coroutineDispatcher: CoroutineDispatcher
        get() = Dispatchers.IO
}