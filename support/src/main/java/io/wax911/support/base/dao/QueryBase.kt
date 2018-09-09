package io.wax911.support.base.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

interface QueryBase<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(attribute: T)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(attributes:List<T>)

    @Update
    fun update(attribute: T)

    @Update
    fun update(attributes:List<T>)

    @Delete
    fun delete(attribute: T)

    @Delete
    fun delete(attributes:List<T>)

    fun get() : T
    fun get(id : Long) : T
    fun get(offset : Int, limit : Int) : List<T>
}
