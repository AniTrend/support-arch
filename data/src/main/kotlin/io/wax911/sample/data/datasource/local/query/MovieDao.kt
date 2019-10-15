package io.wax911.sample.data.datasource.local.query

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import co.anitrend.arch.data.dao.ISupportQuery
import io.wax911.sample.data.entitiy.movie.MovieEntity

@Dao
interface MovieDao: ISupportQuery<MovieEntity> {

    @Query("select count(id) from MovieEntity")
    suspend fun count(): Int

    @Query("select * from MovieEntity order by votes desc")
    fun getPopularItems(): DataSource.Factory<Int, MovieEntity>

    @Query("select * from MovieEntity order by trendingRank")
    fun getTrendingItems(): DataSource.Factory<Int, MovieEntity>

    @Query("delete from MovieEntity")
    suspend fun deleteAll()
}