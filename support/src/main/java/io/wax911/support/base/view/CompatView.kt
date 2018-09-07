package io.wax911.support.base.view

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.Observer
import io.wax911.support.base.dao.CrudRepository
import io.wax911.support.base.event.ResponseCallback

import io.wax911.support.custom.presenter.SupportPresenter
import io.wax911.support.custom.viewmodel.SupportViewModel

interface CompatView<VM> : Observer<VM>, SharedPreferences.OnSharedPreferenceChangeListener, ResponseCallback<VM> {

    fun requestPermissionIfMissing(permission: String): Boolean

    fun <T : Application> getApplicationBase(): T

    fun updateUI()

    fun makeRequest()

    fun getViewName() : String

    /**
     * Informs parent activity if on back can continue to super method or not
     */
    fun hasBackPressableAction(): Boolean = false
}
