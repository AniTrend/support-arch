package io.wax911.sample.core.dao.query

import androidx.room.Dao
import androidx.room.Query
import io.wax911.sample.core.model.show.Show
import io.wax911.support.core.dao.ISupportQuery

@Dao
interface ShowDao: ISupportQuery<Show> {

    @Query("select count(id) from Show")
    suspend fun count(): Int
}