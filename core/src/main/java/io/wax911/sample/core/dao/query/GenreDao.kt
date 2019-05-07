package io.wax911.sample.core.dao.query

import androidx.room.Dao
import androidx.room.Query
import io.wax911.sample.core.auth.model.JsonWebToken
import io.wax911.sample.core.model.meta.Genre
import io.wax911.support.core.dao.ISupportQuery

@Dao
interface GenreDao: ISupportQuery<Genre?> {

    @Query("select count(name) from Genre")
    fun count(): Int

    @Query("select * from Genre")
    fun findAll(): List<Genre>?

    @Query("select * from Genre limit :limit offset :offset")
    fun findAll(offset: Int, limit: Int): List<Genre>?
}