package io.wax911.sample.core.dao.query

import androidx.room.Dao
import androidx.room.Query
import io.wax911.sample.core.model.attribute.Language
import io.wax911.support.core.dao.ISupportQuery

@Dao
interface LanguageDao: ISupportQuery<Language?> {

    @Query("select count(name) from Language")
    suspend fun count(): Int

    @Query("select * from Language")
    suspend fun findAll(): List<Language>?

    @Query("select * from Language limit :limit offset :offset")
    suspend fun findAll(offset: Int, limit: Int): List<Language>?
}