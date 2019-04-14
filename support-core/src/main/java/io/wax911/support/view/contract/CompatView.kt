package io.wax911.support.view.contract

import android.content.SharedPreferences
import android.os.Bundle
import androidx.lifecycle.Observer
import io.wax911.support.activity.SupportActivity
import io.wax911.support.fragment.SupportFragment
import io.wax911.support.repository.SupportRepository
import io.wax911.support.presenter.SupportPresenter
import io.wax911.support.viewmodel.SupportViewModel

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
     * Used by [SupportFragment.presenter] and [SupportActivity.presenter] lazy delegate
     * to create a presenter extending [SupportPresenter]
     *
     * @return [SupportPresenter] that will be used by the fragment activity
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

    /**
     * Additional initialization to be done in this method, if the overriding class is type of [SupportFragment]
     * then this method will be called in [SupportFragment.onCreate]. Otherwise [SupportActivity.onPostCreate]
     * invokes this function
     *
     * @see [SupportActivity.onPostCreate] and [SupportFragment.onCreate]
     * @param
     */
    fun initializeComponents(savedInstanceState: Bundle?)

    fun shouldDisableMenu() : Boolean = false
    fun shouldSubscribe() : Boolean = false

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

    companion object {

        /**
         * Constant value that indicates that no dynamic menu will be inflated for a [SupportFragment]
         *
         * [NO_MENU_ITEM] has the default value of 0
         */
        const val NO_MENU_ITEM = 0
    }
}
