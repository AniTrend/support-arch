package io.wax911.support.view.contract

import android.content.SharedPreferences
import androidx.lifecycle.Observer
import io.wax911.support.presenter.SupportPresenter
import io.wax911.support.viewmodel.SupportViewModel

interface CompatView<VM, P : SupportPresenter<*>> : Observer<VM?>, SharedPreferences.OnSharedPreferenceChangeListener {

    val compatViewPermissionKey: Int
        get() = 110

    /**
     * @return the presenter that will be used by the fragment activity
     */
    fun initPresenter() : P

    /**
     * @return the target view model, assuming that the fragment activity needs one
     */
    fun initViewModel() : SupportViewModel<VM?, *>? {
        throw NotImplementedError("Did you forget to override " +
                "`initViewModel() : SupportViewModel<VM?, *>?` " +
                "in your activity/fragment -> ${getViewName()}")
    }

    fun shouldDisableMenu() : Boolean = false
    fun shouldSubscribe() : Boolean = false

    /**
     * Update views or bind a liveData to them
     */
    fun updateUI()
    fun makeRequest()

    /**
     * Get the current to string of this instance
     */
    fun getViewName() : String = toString()

    /**
     * Check if the current fragment activity has a permission granted to it.
     * If no permission is granted then this method will request a permission for you
     *
     * @see [Activity#requestPermissions]
     */
    fun requestPermissionIfMissing(manifestPermission: String): Boolean {
        return false
    }

    /**
     * Informs parent activity if on back can continue to super method or not
     */
    fun hasBackPressableAction(): Boolean = false
}
