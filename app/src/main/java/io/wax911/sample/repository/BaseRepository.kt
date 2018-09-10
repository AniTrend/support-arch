package io.wax911.sample.repository

import io.wax911.sample.api.NetworkClient
import io.wax911.sample.model.BaseModel
import io.wax911.support.base.dao.CrudRepository
import io.wax911.support.base.event.ResponseCallback
import io.wax911.support.custom.worker.SupportRequestClient
import io.wax911.support.util.InstanceUtil
import retrofit2.Call
import retrofit2.Response

class BaseRepository private constructor(responseCallback: ResponseCallback<BaseModel>) : CrudRepository<Long, BaseModel>(responseCallback) {

    override fun save(model: BaseModel) {
        modelDao.insert(model)
    }

    override fun find(key: Long) {
        val result = modelDao.get(key)
        mutableLiveData.value = result
    }

    override fun delete(model: BaseModel) {
        modelDao.delete(model)
    }

    /**
     * Creates the network client for implementing class
     */
    override fun createNetworkClient(): SupportRequestClient<BaseModel> =
            NetworkClient.newInstance(this, parameters)

    /**
     * When the application is not connected to the internet this method is called to resolve the
     * kind of content that needs to be fetched from the database
     */
    override fun requestFromCache() {
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

    companion object : InstanceUtil<CrudRepository<Long, BaseModel>, ResponseCallback<BaseModel>>({ BaseRepository(it) })
}
