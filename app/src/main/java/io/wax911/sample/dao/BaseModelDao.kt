package io.wax911.sample.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import io.wax911.sample.model.BaseModel
import io.wax911.support.base.dao.QueryBase

@Dao
interface BaseModelDao : QueryBase<BaseModel> {

    @Query("select * from BaseModel limit 1")
    override fun get(): LiveData<BaseModel>

    @Query("select * from BaseModel where id = :id")
    override fun get(id: Long): LiveData<BaseModel>

    @Query("select * from BaseModel limit :limit offset :offset")
    override fun get(offset: Int, limit: Int): LiveData<List<BaseModel>>
}