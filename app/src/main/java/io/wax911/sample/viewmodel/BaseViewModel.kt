package io.wax911.sample.viewmodel

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import io.wax911.sample.model.BaseModel
import io.wax911.sample.repository.BaseRepository
import io.wax911.sample.util.getDatabase
import io.wax911.support.viewmodel.CompanionViewModel
import io.wax911.support.viewmodel.SupportViewModel
import io.wax911.support.getViewModelOf

class BaseViewModel : SupportViewModel<BaseModel?, Long>() {

    companion object : CompanionViewModel<BaseViewModel, BaseModel> {

        /**
         * Returns the view model that should be used by activities and fragments.
         *
         * @param context valid null checked reference to a fragment context
         * @param observer type to which should be used when observing changes
         */
        override fun newInstance(context: FragmentActivity, observer: Observer<BaseModel?>) =
                context.getViewModelOf<BaseViewModel>().also {
                    it.repository = BaseRepository.newInstance(context.getDatabase())
                    it.repository.registerObserver(context, observer)
                }
    }
}