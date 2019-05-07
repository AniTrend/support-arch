package io.wax911.sample.core.dao.query

import androidx.room.Dao
import androidx.room.Query
import io.wax911.sample.core.model.meta.Country
import io.wax911.support.core.dao.ISupportQuery

@Dao
interface CountryDao: ISupportQuery<Country?> {

    @Query("select count(name) from Country")
    fun count(): Int

    @Query("select * from Country")
    fun findAll(): List<Country>?

    @Query("select * from Country limit :limit offset :offset")
    fun findAll(offset: Int, limit: Int): List<Country>?
}