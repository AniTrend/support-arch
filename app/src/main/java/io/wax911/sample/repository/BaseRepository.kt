package io.wax911.sample.repository

import android.os.Bundle
import androidx.lifecycle.Observer
import io.wax911.sample.api.NetworkClient
import io.wax911.sample.dao.BaseModelDao
import io.wax911.sample.dao.DatabaseHelper
import io.wax911.sample.model.BaseModel
import io.wax911.support.base.dao.CrudRepository
import io.wax911.support.base.event.ResponseCallback
import io.wax911.support.custom.worker.SupportRequestClient
import retrofit2.Call
import retrofit2.Response

class BaseRepository private constructor() : CrudRepository<Long, BaseModel>() {

    private lateinit var modelDao: BaseModelDao

    override fun save(model: BaseModel) {
        modelDao.insert(model)
    }

    override fun findOne(key: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun findAll() {
        model.value = modelDao.get().value
    }

    override fun delete(model: BaseModel) {
        modelDao.delete(model)
    }

    /**
     * Creates the network client for implementing class
     *
     * @param parameters bundle of parameters for the request
     */
    override fun createNetworkClient(parameters: Bundle): SupportRequestClient<BaseModel> =
            NetworkClient.newInstance(this, parameters)

    /**
     * When the application is not connected to the internet this method is called to resolve the
     * kind of content that needs to be fetched from the database
     *
     * @param parameters bundle of parameters for the request
     */
    override fun requestFromCache(parameters: Bundle) {
        when (requestType) {

        }
    }

    /**
     * Invoked for a received HTTP response.
     *
     *
     * Note: An HTTP response may still indicate an application-level failure such as a 404 or 500.
     * Call [Response.isSuccessful] to determine if the response indicates success.
     *
     * @param call the origination requesting object
     * @param response the response from the network
     */
    override fun onResponse(call: Call<BaseModel>, response: Response<BaseModel>) {
        when (requestType) {

        }
    }

    companion object {
        fun newInstance(responseCallback: ResponseCallback<BaseModel>?, databaseHelper: DatabaseHelper) : CrudRepository<Long, BaseModel> {
            val repository = BaseRepository()
            repository.modelDao = databaseHelper.baseModelDao()
            repository.responseCallback = responseCallback
            return repository
        }
    }

    override fun onCleared() {

    }
}
