package io.wax911.sample.viewmodel

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import io.wax911.sample.model.BaseModel
import io.wax911.sample.repository.BaseRepository
import io.wax911.sample.util.getDatabase
import io.wax911.support.base.event.ResponseCallback
import io.wax911.support.custom.viewmodel.SupportViewModel

class BaseViewModel : SupportViewModel<BaseModel, Long>() {

    companion object {
        fun newInstance(context: FragmentActivity, responseCallback: ResponseCallback<BaseModel>) : BaseViewModel {
            val viewModel = ViewModelProviders.of(context).get(BaseViewModel::class.java)
            val repository = BaseRepository.newInstance(responseCallback)
            repository.modelDao = context.getDatabase().baseModelDao()
            viewModel.initDependencies(repository)
            return viewModel
        }
    }
}