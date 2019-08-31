package co.anitrend.arch.ui.view.contract

import android.content.SharedPreferences
import android.os.Bundle
import co.anitrend.arch.core.presenter.SupportPresenter
import co.anitrend.arch.core.viewmodel.SupportViewModel
import co.anitrend.arch.extension.util.SupportCoroutineHelper

/**
 * Contract for implementing [androidx.fragment.app.FragmentActivity] based components
 *
 * @since 0.9.X
 * @see SupportCoroutineHelper
 */
interface ISupportFragmentActivity<VM, P : SupportPresenter<*>> : SupportCoroutineHelper,
    SharedPreferences.OnSharedPreferenceChangeListener {

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
     * @return supportPresenter of the generic type specified
     */
    val supportPresenter: P


    /**
     * Should be created lazily through injection or lazy delegate
     *
     * @return view model of the given type
     */
    val supportViewModel: SupportViewModel<*, VM>?
        get() = null

    /**
     * Used to dynamically enable or disable context menu
     */
    val isMenuEnabled: Boolean
        get() = true

    /**
     * Additional initialization to be done in this method, if the overriding class is type of
     * [androidx.fragment.app.Fragment] then this method will be called in
     * [androidx.fragment.app.FragmentActivity.onCreate]. Otherwise
     * [androidx.fragment.app.FragmentActivity.onPostCreate] invokes this function
     *
     * @param savedInstanceState
     */
    fun initializeComponents(savedInstanceState: Bundle?)


    /**
     * Handles the updating of views, binding, creation or state change, depending on the context
     * [androidx.lifecycle.LiveData] for a given [ISupportFragmentActivity] will be available by this point.
     *
     * Check implementation for more details
     */
    fun onUpdateUserInterface()

    /**
     * Handles the complex logic required to dispatch network request to [SupportViewModel]
     * to either request from the network or database cache.
     *
     * The results of the dispatched network or cache call will be published by the
     * [androidx.lifecycle.LiveData] specifically [SupportViewModel.model]
     *
     * @see [SupportViewModel.requestBundleLiveData]
     */
    fun onFetchDataInitialize()

    /**
     * Check if the current fragment activity has a permission granted to it.
     * If no permission is granted then this method will request a permission for you
     *
     * @see [androidx.fragment.app.FragmentActivity.requestPermissions]
     */
    fun requestPermissionIfMissing(manifestPermission: String): Boolean = false

    /**
     * Returns a boolean that can be used to block/unblock [androidx.fragment.app.FragmentActivity.onBackPressed]
     * from calling it's super method to finish an activity.
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
         * Constant value that indicates that no dynamic menu will be inflated for a [CustomView]
         *
         * [NO_MENU_ITEM] has the default value of 0
         */
        const val NO_MENU_ITEM = 0
    }
}
