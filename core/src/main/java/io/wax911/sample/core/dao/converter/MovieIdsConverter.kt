package io.wax911.sample.core.dao.converter

import androidx.room.TypeConverter
import io.wax911.sample.core.api.RetroFactory
import io.wax911.sample.core.extension.getTypeToken
import io.wax911.sample.core.model.movie.contract.MovieIds
import io.wax911.support.core.dao.RoomConverter

class MovieIdsConverter: RoomConverter<MovieIds> {

    /**
     * Convert database types back to the original type
     *
     * @see androidx.room.TypeConverter
     * @param dbValue saved database value type
     */
    @TypeConverter
    override fun fromDatabaseValue(dbValue: String): MovieIds? {
        return RetroFactory.gson.fromJson(dbValue, getTypeToken<MovieIds>())
    }

    /**
     * Convert custom types to database values that room can persist,
     * recommended persistence format is json strings.
     *
     * @see androidx.room.TypeConverter
     * @param entity item which room should convert
     */
    @TypeConverter
    override fun toDatabaseValue(entity: MovieIds?): String {
        return RetroFactory.gson.toJson(entity)
    }
}