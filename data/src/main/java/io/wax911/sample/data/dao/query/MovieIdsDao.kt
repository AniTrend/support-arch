package io.wax911.sample.data.dao.query

import androidx.room.Dao
import androidx.room.Query
import io.wax911.sample.data.model.movie.contract.MovieIds
import io.wax911.support.data.dao.ISupportQuery

@Dao
interface MovieIdsDao: ISupportQuery<MovieIds> {

    @Query("select count(trakt) from MovieIds")
    suspend fun count(): Int
}