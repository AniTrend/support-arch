package io.wax911.sample.data.dao.query

import androidx.room.Dao
import androidx.room.Query
import io.wax911.sample.data.model.movie.Movie
import io.wax911.support.core.dao.ISupportQuery

@Dao
interface MovieDao: ISupportQuery<Movie> {

    @Query("select count(id) from Movie")
    suspend fun count(): Int
}