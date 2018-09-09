package io.wax911.support.base.view

import android.content.SharedPreferences
import androidx.lifecycle.Observer
import io.wax911.support.base.event.ResponseCallback

interface CompatView<VM> : Observer<VM>, SharedPreferences.OnSharedPreferenceChangeListener, ResponseCallback<VM> {

    val compatViewPermissionKey: Int
        get() = 110

    /**
     * Mandatory presenter initialization
     */
    fun initPresenter()

    /**
     * Update views or bind a mutableLiveData to them
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
     * @see ActivityCompat#requestPermissions
     */
    fun requestPermissionIfMissing(manifestPermission: String): Boolean

    /**
     * Informs parent activity if on back can continue to super method or not
     */
    fun hasBackPressableAction(): Boolean = false
}
