package io.wax911.support.base.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

interface SupportQuery<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(attribute: T)

    @Update
    fun update(attribute: T)

    @Delete
    fun delete(attribute: T)
}
