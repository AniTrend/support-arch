package io.wax911.sample.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.wax911.support.base.dao.CrudRepository
import io.wax911.support.custom.repository.SupportRepository
import io.wax911.sample.model.BaseModel

class BaseRepository : CrudRepository<Long, BaseModel> {

    override fun count(): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun save(model: BaseModel): LiveData<BaseModel> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun save(model: List<BaseModel>): LiveData<List<BaseModel>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun findOne(key: Long): LiveData<BaseModel> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun findAll(): LiveData<List<BaseModel>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun findAll(keys: List<Long>): LiveData<List<BaseModel>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(model: BaseModel) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteAll() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}
