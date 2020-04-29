package co.anitrend.arch.data.mapper.contract

/**
 * Contract for handling network responses to mapping flow
 *
 * @since v1.1.0
 */
@Deprecated(
    "Consider using converter instead for better coverage",
    ReplaceWith(
        expression = "SupportConverter",
        imports = arrayOf("co.anitrend.arch.data.converter.SupportConverter")
    )
)
interface ISupportResponseMapper<in S, D> {

    /**
     * Creates mapped objects and handles the database operations which may be required to map various objects,
     * called in [retrofit2.Callback.onResponse] after assuring that the response was a success
     *
     * @param source the incoming data source type
     * @return Mapped object that will be consumed by [onResponseDatabaseInsert]
     */
    suspend fun onResponseMapFrom(source: S): D

    /**
     * Inserts the given object into the implemented room database,
     * called in [retrofit2.Callback.onResponse]
     *
     * @param mappedData mapped object from [onResponseMapFrom] to insert into the database
     */
    suspend fun onResponseDatabaseInsert(mappedData: D)
}