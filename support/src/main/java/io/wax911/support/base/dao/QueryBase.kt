package io.wax911.support.base.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update

interface QueryBase<T> {

    @Insert
    fun insert(attribute: T)

    @Insert
    fun insert(attributes:List<T>)

    @Update
    fun update(attribute: T)

    @Update
    fun update(attributes:List<T>)

    @Delete
    fun delete(attribute: T)

    @Delete
    fun delete(attributes:List<T>)
}
