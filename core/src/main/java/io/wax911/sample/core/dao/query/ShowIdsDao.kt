package io.wax911.sample.core.dao.query

import androidx.room.Dao
import androidx.room.Query
import io.wax911.sample.core.model.show.contract.ShowIds
import io.wax911.support.core.dao.ISupportQuery

@Dao
interface ShowIdsDao: ISupportQuery<ShowIds> {

    @Query("select count(trakt) from ShowIds")
    suspend fun count(): Int
}