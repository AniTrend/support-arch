package co.anitrend.arch.data.mapper.contract

import co.anitrend.arch.extension.util.SupportCoroutineHelper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * Contract for handling network responses to mapping flow
 *
 * @since v1.1.0
 */
interface ISupportDataMapper<in S, D> : SupportCoroutineHelper {

    /**
     * Creates mapped objects and handles the database operations which may be required to map various objects,
     * called in [retrofit2.Callback.onResponse] after assuring that the response was a success
     *
     * @param source the incoming data source type
     * @return Mapped object that will be consumed by [onResponseDatabaseInsert]
     *
     * @see [IMapperHelper.invoke]
     * @see [mapFrom]
     */
    suspend fun onResponseMapFrom(source: S): D

    /**
     * Inserts the given object into the implemented room database,
     * called in [retrofit2.Callback.onResponse]
     *
     * @param mappedData mapped object from [onResponseMapFrom] to insert into the database
     *
     * @see [IMapperHelper.invoke]
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