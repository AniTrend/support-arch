package io.wax911.sample.presenter

import android.content.Context

import io.wax911.support.custom.presenter.SupportPresenter
import io.wax911.sample.util.Settings
import io.wax911.support.util.InstanceUtil

class BasePresenter private constructor(context: Context?): SupportPresenter<Settings>(context) {

    /**
     * Provides the preference object to the lazy initializer
     */
    override fun createPreference(): Settings? =
            context?.let{ Settings.newInstance(it) }

    companion object : InstanceUtil<BasePresenter, Context?>({ BasePresenter(it) })
}
