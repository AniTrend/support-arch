package io.wax911.support.base.dao

import androidx.lifecycle.LiveData

interface CrudRepository<K, V> {

    fun count() : Long

    fun save(model : V) : LiveData<V>
    fun save(model : List<V>) : LiveData<List<V>>

    fun findOne(key : K) : LiveData<V>
    fun findAll() : LiveData<List<V>>
    fun findAll(keys: List<K>) : LiveData<List<V>>

    fun delete(model : V)
    fun deleteAll()
}
