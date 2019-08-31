package io.wax911.sample.data.datasource.local.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import io.wax911.sample.data.extension.getTypeToken
import io.wax911.sample.data.entitiy.show.contract.ShowEntityIds
import co.anitrend.arch.data.dao.RoomConverter
import org.koin.core.KoinComponent
import org.koin.core.get

class ShowIdsConverter: RoomConverter<ShowEntityIds>, KoinComponent {

    /**
     * Convert database types back to the original type
     *
     * @see androidx.room.TypeConverter
     * @param dbValue saved database value type
     */
    @TypeConverter
    override fun fromDatabaseValue(dbValue: String): ShowEntityIds? {
        return get<Gson>().fromJson(dbValue, getTypeToken<ShowEntityIds>())
    }

    /**
     * Convert custom types to database values that room can persist,
     * recommended persistence format is json strings.
     *
     * @see androidx.room.TypeConverter
     * @param entity item which room should convert
     */
    @TypeConverter
    override fun toDatabaseValue(entity: ShowEntityIds?): String {
        return get<Gson>().toJson(entity)
    }
}