package io.wax911.sample.data.dao.query

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import io.wax911.sample.data.model.movie.Movie
import io.wax911.support.data.dao.ISupportQuery

@Dao
interface MovieDao: ISupportQuery<Movie> {

    @Query("select count(id) from Movie")
    suspend fun count(): Int

    @Query("select * from Movie order by votes desc")
    fun getPopularItems(): DataSource.Factory<Int, Movie>

    @Query("select * from Movie order by trendingRank")
    fun getTrendingItems(): DataSource.Factory<Int, Movie>

    @Query("select * from Movie order by anticipationRank")
    fun getAnticipatedItems(): DataSource.Factory<Int, Movie>

    @Query("delete from Movie")
    suspend fun deleteAll()
}