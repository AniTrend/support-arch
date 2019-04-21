package io.wax911.sample.dao

import androidx.room.Dao
import androidx.room.Query
import io.wax911.sample.model.WebToken
import io.wax911.support.core.dao.ISupportQuery

@Dao
interface WebTokenDao : ISupportQuery<WebToken> {

    @Query("select * from WebToken limit 1")
    fun get(): WebToken

    @Query("select * from WebToken where id = :id")
    fun get(id: Long): WebToken
}
