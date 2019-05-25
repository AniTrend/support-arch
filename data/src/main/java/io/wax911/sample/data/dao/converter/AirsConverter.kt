package io.wax911.sample.data.dao.converter

import androidx.room.TypeConverter
import io.wax911.sample.data.api.RetroFactory
import io.wax911.sample.data.extension.getTypeToken
import io.wax911.sample.data.model.show.meta.Airs
import io.wax911.support.core.dao.RoomConverter

class AirsConverter: RoomConverter<Airs> {

    /**
     * Convert database types back to the original type
     *
     * @see androidx.room.TypeConverter
     * @param dbValue saved database value type
     */
    @TypeConverter
    override fun fromDatabaseValue(dbValue: String): Airs? {
        return RetroFactory.gson.fromJson(dbValue, getTypeToken<Airs>())
    }

    /**
     * Convert custom types to database values that room can persist,
     * recommended persistence format is json strings.
     *
     * @see androidx.room.TypeConverter
     * @param entity item which room should convert
     */
    @TypeConverter
    override fun toDatabaseValue(entity: Airs?): String {
        return RetroFactory.gson.toJson(entity)
    }
}