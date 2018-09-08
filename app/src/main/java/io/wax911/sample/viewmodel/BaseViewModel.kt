package io.wax911.sample.viewmodel

import android.content.Context
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import io.wax911.sample.api.NetworkClient
import io.wax911.sample.model.BaseModel
import io.wax911.sample.repository.BaseRepository
import io.wax911.sample.util.getDatabase
import io.wax911.support.base.event.ResponseCallback
import io.wax911.support.custom.viewmodel.SupportViewModel
import retrofit2.Call
import retrofit2.Response

class BaseViewModel : SupportViewModel<BaseModel, Long>() {

    override fun queryFor(requestType: Int, context: Context) =
        repository.requestFromNetwork(requestType, bundle, context)


    companion object {
        fun newInstance(context: FragmentActivity, responseCallback: ResponseCallback<BaseModel>) : BaseViewModel {
            val viewModel = ViewModelProviders.of(context).get(BaseViewModel::class.java)
            viewModel.initDependencies(BaseRepository.newInstance(responseCallback, context.getDatabase()))
            return viewModel
        }
    }
}