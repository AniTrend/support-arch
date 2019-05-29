package io.wax911.support.core.presenter

import android.content.Context
import android.content.SharedPreferences
import io.wax911.support.data.util.pagination.SupportPagingHelper
import io.wax911.support.extension.preference.SupportPreference
import io.wax911.support.extension.preference.event.OnSharedPreferenceBinder
import io.wax911.support.extension.util.SupportExtKeyStore

abstract class SupportPresenter<S : SupportPreference>(
    protected val context: Context?,
    val supportPreference: S
): OnSharedPreferenceBinder {

    val pagingHelper
        get() = SupportPagingHelper(
            pageSize = paginationSize(),
            isPagingLimit = false
        )

    protected open fun paginationSize(): Int = SupportExtKeyStore.pagingLimit

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
        supportPreference.sharedPreferences
                .unregisterOnSharedPreferenceChangeListener(changeListener)
    }

    /**
     * Register any listeners from fragments or activities
     */
    override fun onResume(changeListener: SharedPreferences.OnSharedPreferenceChangeListener) {
        supportPreference.sharedPreferences
                .registerOnSharedPreferenceChangeListener(changeListener)
    }
}
