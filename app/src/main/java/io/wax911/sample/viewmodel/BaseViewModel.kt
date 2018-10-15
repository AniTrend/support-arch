package io.wax911.sample.viewmodel

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import io.wax911.sample.model.BaseModel
import io.wax911.sample.repository.BaseRepository
import io.wax911.sample.util.getDatabase
import io.wax911.support.custom.viewmodel.SupportViewModel

class BaseViewModel : SupportViewModel<BaseModel?, Long>() {

    companion object {
        fun newInstance(context: FragmentActivity, observer: Observer<BaseModel?>) : BaseViewModel {
            val viewModel = ViewModelProviders.of(context).get(BaseViewModel::class.java)
            viewModel.repository = BaseRepository.newInstance(context.getDatabase())
            viewModel.repository.registerObserver(context, observer)
            return viewModel
        }
    }
}