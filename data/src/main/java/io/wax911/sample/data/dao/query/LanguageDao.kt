package io.wax911.sample.data.dao.query

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import io.wax911.sample.data.model.attribute.Language
import io.wax911.support.data.dao.ISupportQuery

@Dao
interface LanguageDao: ISupportQuery<Language?> {

    @Query("select count(name) from Language")
    suspend fun count(): Int

    @Query("select * from Language")
    suspend fun findAll(): List<Language>?

    @Query("select * from Language limit :limit offset :offset")
    suspend fun findAll(offset: Int, limit: Int): List<Language>?

    @Query("delete from Language")
    suspend fun deleteAll()

    @Query("select * from Language where name = :name order by name asc")
    fun findLiveData(name: String): LiveData<List<Language>>?

    @Query("select * from Language order by name asc")
    fun findAllLiveData(): LiveData<List<Language>?>
}