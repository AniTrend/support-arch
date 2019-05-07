package io.wax911.sample.core.dao.query

import androidx.room.Dao
import androidx.room.Query
import io.wax911.sample.core.model.movie.Movie
import io.wax911.support.core.dao.ISupportQuery

@Dao
interface MovieDao: ISupportQuery<Movie> {

    @Query("select count(id) from Movie")
    fun count(): Int
}