package io.wax911.sample.data.dao.query

import androidx.room.Dao
import androidx.room.Query
import io.wax911.sample.data.model.attribute.Genre
import io.wax911.support.core.dao.ISupportQuery

@Dao
interface GenreDao: ISupportQuery<Genre?> {

    @Query("select count(name) from Genre")
    suspend fun count(): Int

    @Query("select * from Genre")
    suspend fun findAll(): List<Genre>?

    @Query("select * from Genre limit :limit offset :offset")
    suspend fun findAll(offset: Int, limit: Int): List<Genre>?
}