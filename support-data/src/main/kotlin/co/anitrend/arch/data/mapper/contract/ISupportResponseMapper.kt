package co.anitrend.arch.data.mapper.contract

/**
 * Contract for handling network responses to mapping flow
 *
 * @since v1.1.0
 */
interface ISupportResponseMapper<in S, D> {

    /**
     * Creates mapped objects and handles the database operations which may be required to map various objects,
     *
     * @param source the incoming data source type
     * @return mapped object that will be consumed by [onResponseDatabaseInsert]
     */
    suspend fun onResponseMapFrom(source: S): D

    /**
     * Inserts the given object into the implemented room database,
     *
     * @param mappedData mapped object from [onResponseMapFrom] to insert into the database
     */
    suspend fun onResponseDatabaseInsert(mappedData: D)
}