package io.wax911.support.base.dao

import androidx.lifecycle.LiveData
import androidx.room.*

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

    fun get() : LiveData<T>
    fun get(id : Long) : LiveData<T>
    fun get(offset : Int, limit : Int) : LiveData<List<T>>
}
