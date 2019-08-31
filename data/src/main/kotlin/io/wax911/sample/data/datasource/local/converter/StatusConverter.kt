package io.wax911.sample.data.datasource.local.converter

import androidx.room.TypeConverter
import com.uwetrottmann.trakt5.enums.Status
import io.wax911.sample.data.extension.getTypeToken
import co.anitrend.arch.data.dao.RoomConverter
import org.koin.core.KoinComponent

class StatusConverter: RoomConverter<Status>, KoinComponent {

    /**
     * Convert database types back to the original type
     *
     * @see androidx.room.TypeConverter
     * @param dbValue saved database value type
     */
    @TypeConverter
    override fun fromDatabaseValue(dbValue: String): Status? {
        return Status.fromValue(dbValue)
    }

    /**
     * Convert custom types to database values that room can persist,
     * recommended persistence format is json strings.
     *
     * @see androidx.room.TypeConverter
     * @param entity item which room should convert
     */
    @TypeConverter
    override fun toDatabaseValue(entity: Status?): String {
        return entity?.name ?: Status.IN_PRODUCTION.name
    }
}