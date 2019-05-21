package io.wax911.sample.core.dao.query

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import io.wax911.sample.core.data.entitiy.trending.TrendingMovie
import io.wax911.support.core.dao.ISupportQuery

@Dao
interface TrendingMovieDao: ISupportQuery<TrendingMovie> {

    @Transaction
    @Query("SELECT * FROM TrendingMovie ORDER BY watchers DESC")
    fun getTrendingMovies(): DataSource.Factory<Int, TrendingMovie.MovieRelation>

    @Transaction
    @Query("SELECT * FROM TrendingMovie ORDER BY watchers DESC LIMIT :limit OFFSET :offset")
    fun getTrendingMoviesLiveData(
        limit: Int,
        offset: Int
    ): LiveData<List<TrendingMovie.MovieRelation>>
}