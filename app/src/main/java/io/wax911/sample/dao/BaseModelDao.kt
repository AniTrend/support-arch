package io.wax911.sample.dao

import androidx.room.Dao
import androidx.room.Query
import io.wax911.sample.model.BaseModel
import io.wax911.support.base.dao.SupportQuery

@Dao
interface BaseModelDao : SupportQuery<BaseModel?> {

    @Query("select * from BaseModel limit 1")
    fun get(): BaseModel?

    @Query("select * from BaseModel where id = :id")
    fun get(id: Long): BaseModel?
}