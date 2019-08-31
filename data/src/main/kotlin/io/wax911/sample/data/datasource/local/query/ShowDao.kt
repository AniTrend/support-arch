package io.wax911.sample.data.datasource.local.query

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import io.wax911.sample.data.entitiy.show.ShowEntity
import co.anitrend.arch.data.dao.ISupportQuery

@Dao
interface ShowDao: ISupportQuery<ShowEntity> {

    @Query("select count(id) from ShowEntity")
    suspend fun count(): Int

    @Query("select * from ShowEntity order by votes desc")
    fun getPopularItems(): DataSource.Factory<Int, ShowEntity>

    @Query("select * from ShowEntity order by trendingRank")
    fun getTrendingItems(): DataSource.Factory<Int, ShowEntity>

    @Query("delete from ShowEntity")
    suspend fun deleteAll()
}