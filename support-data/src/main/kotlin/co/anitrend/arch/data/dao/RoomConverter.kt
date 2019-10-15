package co.anitrend.arch.data.dao

/**
 * A helper contract for [androidx.room.TypeConverter]
 *
 * @since v0.9.X
 */
interface RoomConverter<T> {

    /**
     * Convert database types back to the original type
     *
     * @see androidx.room.TypeConverter
     * @param dbValue saved database value type
     */
    fun fromDatabaseValue(dbValue : String) : T?

    /**
     * Convert custom types to database values that room can persist,
     * recommended persistence format is json strings.
     *
     * @see androidx.room.TypeConverter
     * @param entity item which room should convert
     */
    fun toDatabaseValue(entity: T?) : String
}