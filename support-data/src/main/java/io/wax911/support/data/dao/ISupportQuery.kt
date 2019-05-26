package io.wax911.support.data.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

interface ISupportQuery<T> {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vararg attribute: T)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(vararg attribute: T)

    @Delete
    suspend fun delete(vararg attribute: T)
}
