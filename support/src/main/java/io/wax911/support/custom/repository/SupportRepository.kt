package io.wax911.support.custom.repository

import io.wax911.support.base.dao.CrudRepository
import io.wax911.support.base.dao.QueryBase

abstract class SupportRepository<K, V> : CrudRepository<K, V>() {

    lateinit var persistenceModel : QueryBase<V>

    override fun save(model: V) {
        persistenceModel.insert(model)
    }

    override fun save(model: List<V>) {
        persistenceModel.insert(model)
    }

    override fun delete(model: V) {
        persistenceModel.delete(model)
    }
}
