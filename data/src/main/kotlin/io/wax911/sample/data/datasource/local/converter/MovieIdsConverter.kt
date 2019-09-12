package io.wax911.sample.data.datasource.local.converter

import androidx.room.TypeConverter
import co.anitrend.arch.data.dao.RoomConverter
import com.google.gson.Gson
import io.wax911.sample.data.entitiy.movie.contract.MovieEntityIds
import io.wax911.sample.data.extension.getTypeToken
import org.koin.core.KoinComponent
import org.koin.core.get

class MovieIdsConverter: RoomConverter<MovieEntityIds>, KoinComponent {

    /**
     * Convert database types back to the original type
     *
     * @see androidx.room.TypeConverter
     * @param dbValue saved database value type
     */
    @TypeConverter
    override fun fromDatabaseValue(dbValue: String): MovieEntityIds? {
        return get<Gson>().fromJson(dbValue, getTypeToken<MovieEntityIds>())
    }

    /**
     * Convert custom types to database values that room can persist,
     * recommended persistence format is json strings.
     *
     * @see androidx.room.TypeConverter
     * @param entity item which room should convert
     */
    @TypeConverter
    override fun toDatabaseValue(entity: MovieEntityIds?): String {
        return get<Gson>().toJson(entity)
    }
}