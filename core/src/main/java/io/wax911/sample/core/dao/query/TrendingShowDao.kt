package io.wax911.sample.core.dao.query

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import io.wax911.sample.core.data.entitiy.trending.TrendingShow
import io.wax911.support.core.dao.ISupportQuery

@Dao
interface TrendingShowDao: ISupportQuery<TrendingShow> {

    @Transaction
    @Query("SELECT * FROM TrendingShow ORDER BY watchers DESC")
    fun getTrendingShows(): DataSource.Factory<Int, TrendingShow.ShowRelation>

    @Transaction
    @Query("SELECT * FROM TrendingShow ORDER BY watchers DESC LIMIT :limit OFFSET :offset")
    fun getTrendingShowsLiveData(
        limit: Int,
        offset: Int
    ): LiveData<List<TrendingShow.ShowRelation>>
}