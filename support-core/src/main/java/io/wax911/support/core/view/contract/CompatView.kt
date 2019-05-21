package io.wax911.support.core.view.contract

import android.content.SharedPreferences
import android.os.Bundle
import androidx.lifecycle.Observer
import io.wax911.support.core.presenter.SupportPresenter
import io.wax911.support.core.repository.SupportRepository
import io.wax911.support.core.viewmodel.SupportViewModel

interface CompatView<VM, P : SupportPresenter<*>> : Observer<VM?>, SharedPreferences.OnSharedPreferenceChangeListener {

    /**
     * A simple value that can be used when making permission requests,
     * the value can be overridden in the implementing class
     *
     * [compatViewPermissionValue] has a default value of 110
     */
    val compatViewPermissionValue: Int
        get() = 110

    /**
     * Should be created lazily through injection or lazy delegate
     *
     * @return presenter of the generic type specified
     */
    val presenter: P


    /**
     * Should be created lazily through injection or lazy delegate
     *
     * @return view model of the given type
     */
    val viewModel: SupportViewModel<VM?, *>?
        get() = null

    /**
     * Used to dynamically enable or disable context menu
     */
    val isMenuEnabled: Boolean
        get() = true

    /**
     * Additional initialization to be done in this method, if the overriding class is type of [SupportFragment]
     * then this method will be called in [SupportFragment.onCreate]. Otherwise [SupportActivity.onPostCreate]
     * invokes this function
     *
     * @see [SupportActivity.onPostCreate] and [SupportFragment.onCreate]
     * @param
     */
    fun initializeComponents(savedInstanceState: Bundle?)


    /**
     * Handles the updating of views, binding, creation or state change, depending on the context
     * [androidx.lifecycle.LiveData] for a given [CompatView] will be available by this point.
     *
     * Check implementation for more details
     */
    fun updateUI()

    /**
     * Handles the complex logic required to dispatch network request to [SupportViewModel]
     * which uses [SupportRepository] to either request from the network or database cache.
     *
     * The results of the dispatched network or cache call will be published by the
     * [androidx.lifecycle.LiveData] inside of your [SupportRepository]
     *
     * @see [SupportRepository.publishResult]
     */
    fun makeRequest()

    /**
     * Returns the [toString] of the current implementing class
     *
     * @return [String] name of class
     */
    fun getViewName(): String = toString()

    /**
     * Check if the current fragment activity has a permission granted to it.
     * If no permission is granted then this method will request a permission for you
     *
     * @see [SupportActivity.requestPermissions]
     */
    fun requestPermissionIfMissing(manifestPermission: String): Boolean {
        return false
    }

    /**
     * Returns a boolean that can be used to block/unblock [SupportActivity.onBackPressed] from
     * calling it's super method to finish an activity.
     *
     * @return [Boolean] true or false depending on the override implementation
     */
    fun hasBackPressableAction(): Boolean = false

    /**
     * Returns a boolean that either allows or dis-allows this current fragment
     * from refreshing when preferences have been changed.
     *
     * @param key preference key that has been changed
     */
    fun isPreferenceKeyValid(key: String) = true

    companion object {

        /**
         * Constant value that indicates that no dynamic menu will be inflated for a [SupportFragment]
         *
         * [NO_MENU_ITEM] has the default value of 0
         */
        const val NO_MENU_ITEM = 0
    }
}
