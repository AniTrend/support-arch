package io.wax911.support.core.presenter

import android.content.Context
import android.content.SharedPreferences
import io.wax911.support.core.preference.SupportPreference
import io.wax911.support.core.preference.event.OnSharedPreferencesLifecycleBind
import io.wax911.support.core.util.SupportCoroutineHelper
import io.wax911.support.core.util.SupportKeyStore
import io.wax911.support.core.util.pagination.SupportPagingHelper

abstract class SupportPresenter<S : SupportPreference>(
    protected val context: Context?,
    val supportPreference: S?
): OnSharedPreferencesLifecycleBind, SupportCoroutineHelper {

    val pagingHelper
        get() = SupportPagingHelper(
            pageSize = paginationSize(),
            isPagingLimit = false
        )

    protected open fun paginationSize(): Int = SupportKeyStore.pagingLimit

    /**
     * Enables or disables action mode, behaviour should be implemented in your adapter, in ItemClickLister.
     * Default value for this property is false
     */
    var isActionModeEnabled: Boolean = false

    /**
     * Indicates the number of pages that should be retained to either side of the
     * current page in the view hierarchy in an idle state. Pages beyond this
     * limit will be recreated from the adapter when needed.
     *
     * @see [androidx.viewpager.widget.ViewPager.setOffscreenPageLimit]
     */
    var offScreenPagerLimit: Int = 3

    /**
     * Unregister any listeners from fragments or activities
     */
    override fun onPause(changeListener: SharedPreferences.OnSharedPreferenceChangeListener) {
        supportPreference?.sharedPreferences
                ?.unregisterOnSharedPreferenceChangeListener(changeListener)
    }

    /**
     * Register any listeners from fragments or activities
     */
    override fun onResume(changeListener: SharedPreferences.OnSharedPreferenceChangeListener) {
        supportPreference?.sharedPreferences
                ?.registerOnSharedPreferenceChangeListener(changeListener)
    }
}
