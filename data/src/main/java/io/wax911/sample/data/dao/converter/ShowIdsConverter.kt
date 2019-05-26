package io.wax911.sample.data.dao.converter

import androidx.room.TypeConverter
import io.wax911.sample.data.api.RetroFactory
import io.wax911.sample.data.extension.getTypeToken
import io.wax911.sample.data.model.show.contract.ShowIds
import io.wax911.support.data.dao.RoomConverter

class ShowIdsConverter: RoomConverter<ShowIds> {

    /**
     * Convert database types back to the original type
     *
     * @see androidx.room.TypeConverter
     * @param dbValue saved database value type
     */
    @TypeConverter
    override fun fromDatabaseValue(dbValue: String): ShowIds? {
        return RetroFactory.gson.fromJson(dbValue, getTypeToken<ShowIds>())
    }

    /**
     * Convert custom types to database values that room can persist,
     * recommended persistence format is json strings.
     *
     * @see androidx.room.TypeConverter
     * @param entity item which room should convert
     */
    @TypeConverter
    override fun toDatabaseValue(entity: ShowIds?): String {
        return RetroFactory.gson.toJson(entity)
    }
}