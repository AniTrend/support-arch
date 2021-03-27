package co.anitrend.arch.ui.common

import android.os.Bundle
import co.anitrend.arch.core.model.ISupportViewModelState

/**
 * Contract for implementing lifecycle owner controllers such as
 * [androidx.appcompat.app.AppCompatActivity] and
 * [androidx.fragment.app.Fragment]
 *
 * @since v1.3.0
 */
interface ILifecycleController {

    /**
     * Additional initialization to be done in this method, this is called in during
     * [androidx.fragment.app.FragmentActivity.onPostCreate]
     *
     * @param savedInstanceState
     */
    fun initializeComponents(savedInstanceState: Bundle?)

    /**
     * Proxy for a view model state if one exists
     */
    fun viewModelState(): ISupportViewModelState<*>?
}