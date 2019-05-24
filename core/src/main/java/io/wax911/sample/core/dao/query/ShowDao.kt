package io.wax911.sample.core.dao.query

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import io.wax911.sample.core.model.show.Show
import io.wax911.support.core.dao.ISupportQuery

@Dao
interface ShowDao: ISupportQuery<Show> {

    @Query("select count(id) from Show")
    suspend fun count(): Int

    @Query("select * from Show")
    fun getPopularItems(): DataSource.Factory<Int, Show>

    @Query("select * from Show")
    fun getTrendingItems(): DataSource.Factory<Int, Show>

    @Query("select * from Show")
    fun getAnticipatedItems(): DataSource.Factory<Int, Show>

    @Query("delete from Show")
    suspend fun deleteAll()
}