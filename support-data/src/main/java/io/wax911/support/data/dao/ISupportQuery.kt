package io.wax911.support.data.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

interface ISupportQuery<T> {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(attribute: T)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(attribute: T)

    @Delete
    suspend fun delete(attribute: T)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(attribute: List<T>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(attribute: List<T>)

    @Delete
    suspend fun delete(attribute: List<T>)
}
