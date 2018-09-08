package io.wax911.sample.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import io.wax911.sample.model.WebToken
import io.wax911.support.base.dao.QueryBase

@Dao
interface WebTokenDao : QueryBase<WebToken> {

    @Query("select * from WebToken limit 1")
    override fun get(): LiveData<WebToken>

    @Query("select * from WebToken where id = :id")
    override fun get(id: Long): LiveData<WebToken>

    @Query("select * from WebToken limit :limit offset :offset")
    override fun get(offset: Int, limit: Int): LiveData<List<WebToken>>
}
