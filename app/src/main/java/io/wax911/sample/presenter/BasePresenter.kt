package io.wax911.sample.presenter

import android.content.Context
import io.wax911.sample.util.Settings
import io.wax911.sample.util.StateUtil
import io.wax911.support.presenter.SupportPresenter
import io.wax911.support.factory.InstanceCreator

class BasePresenter private constructor(context: Context?): SupportPresenter<Settings>(context) {

    /**
     * Provides pagination size for calculating offsets
     */
    override fun paginationSize() = StateUtil.pagingLimit

    /**
     * Provides the preference object to the lazy initializer
     */
    override fun createPreference(): Settings? =
            context?.let{ Settings.newInstance(it) }

    companion object : InstanceCreator<BasePresenter, Context?>({ BasePresenter(it) })
}
