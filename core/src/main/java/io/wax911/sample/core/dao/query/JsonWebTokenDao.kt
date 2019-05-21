package io.wax911.sample.core.dao.query

import androidx.room.Dao
import androidx.room.Query
import io.wax911.sample.core.auth.model.JsonWebToken
import io.wax911.support.core.dao.ISupportQuery

@Dao
interface JsonWebTokenDao: ISupportQuery<JsonWebToken?> {

    @Query("select count(id) from JsonWebToken")
    suspend fun count(): Int

    @Query("select * from JsonWebToken order by id asc limit 1")
    suspend fun findLatest(): JsonWebToken?

    @Query("select * from JsonWebToken limit :limit offset :offset")
    suspend fun findAll(offset: Int, limit: Int): List<JsonWebToken>?
}