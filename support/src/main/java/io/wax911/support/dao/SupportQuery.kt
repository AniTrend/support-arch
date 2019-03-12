package io.wax911.support.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

interface SupportQuery<T> {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(attribute: T)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(attribute: T)

    @Delete
    fun delete(attribute: T)
}
