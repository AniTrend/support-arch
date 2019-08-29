package io.wax911.sample.data.dao.query

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import io.wax911.sample.data.model.attribute.Genre
import io.wax911.support.data.dao.ISupportQuery

@Dao
interface GenreDao: ISupportQuery<Genre?> {

    @Query("select count(name) from Genre")
    suspend fun count(): Int

    @Query("select * from Genre")
    suspend fun findAll(): List<Genre>?

    @Query("select * from Genre limit :limit offset :offset")
    suspend fun findAll(offset: Int, limit: Int): List<Genre>?

    @Query("delete from Genre")
    suspend fun deleteAll()

    @Query("select * from Genre where name = :name order by name asc")
    fun findLiveData(name: String): LiveData<List<Genre>>?

    @Query("select * from Genre order by name asc")
    fun findAllLiveData(): LiveData<List<Genre>?>
}