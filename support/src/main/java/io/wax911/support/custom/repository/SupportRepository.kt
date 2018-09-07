package io.wax911.support.custom.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.wax911.support.base.dao.CrudRepository
import io.wax911.support.base.dao.QueryBase

abstract class SupportRepository<K, V> : CrudRepository<K, V> {

    lateinit var persistenceModel : QueryBase<V>

    override fun save(model: V): LiveData<V> {
        persistenceModel.insert(model)
        return MutableLiveData<V>()
    }

    override fun save(model: List<V>): LiveData<List<V>> {
        persistenceModel.insert(model)
        return MutableLiveData<List<V>>()
    }

    override fun delete(model: V) {
        persistenceModel.delete(model)
    }
}
