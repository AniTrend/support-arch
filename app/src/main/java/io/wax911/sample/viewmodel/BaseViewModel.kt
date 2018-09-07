package io.wax911.sample.viewmodel

import android.content.Context
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import io.wax911.sample.api.NetworkClient
import io.wax911.sample.model.BaseModel
import io.wax911.sample.repository.BaseRepository
import io.wax911.support.base.event.ResponseCallback
import io.wax911.support.custom.viewmodel.SupportViewModel
import retrofit2.Call
import retrofit2.Response

class BaseViewModel : SupportViewModel<BaseModel, Long>() {

    override fun requestData(requestType: Int, context: Context) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        fun createInstance(context: FragmentActivity, responseCallback: ResponseCallback<BaseModel>, observer: Observer<BaseModel>) : BaseViewModel {
            val viewModel = ViewModelProviders.of(context).get(BaseViewModel::class.java)
            val networkClient = NetworkClient.createInstance(viewModel)
            viewModel.initDependencies(responseCallback, networkClient, BaseRepository())
            if (!viewModel.model.hasActiveObservers())
                viewModel.model.observe(context, observer)
            return viewModel
        }
    }
}