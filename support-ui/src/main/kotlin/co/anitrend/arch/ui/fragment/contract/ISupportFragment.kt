package co.anitrend.arch.ui.fragment.contract

import android.os.Bundle
import co.anitrend.arch.core.model.ISupportViewModelState

/**
 * Contract for implementing [androidx.fragment.app.FragmentActivity] based components
 *
 * @since v0.9.X
 */
interface ISupportFragment {

    val moduleTag: String

    /**
     * Proxy for a view model state if one exists
     */
    fun viewModelState(): ISupportViewModelState<*>?

    /**
     * Additional initialization to be done in this method, this method will be called in
     * [androidx.fragment.app.FragmentActivity.onCreate].
     *
     * @param savedInstanceState
     */
    fun initializeComponents(savedInstanceState: Bundle?)

    companion object {

        /**
         * Constant value that indicates that no dynamic menu will be inflated for a [CustomView]
         *
         * [NO_MENU_ITEM] has the default value of 0
         */
        const val NO_MENU_ITEM = 0

        /**
         * Constant value that indicates that no dynamic layout will be inflated for a
         * [ISupportFragment] derivative
         *
         * [NO_LAYOUT_ITEM] has the default value of 0
         */
        const val NO_LAYOUT_ITEM = 0
    }
}
