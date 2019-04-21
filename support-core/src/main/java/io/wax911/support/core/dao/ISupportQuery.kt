package io.wax911.support.core.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

interface ISupportQuery<T> {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(vararg attribute: T)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(vararg attribute: T)

    @Delete
    fun delete(vararg attribute: T)
}
