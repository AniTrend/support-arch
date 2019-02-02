package io.wax911.support.custom.presenter

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import io.wax911.support.event.LifecycleListener
import io.wax911.support.custom.preference.SupportPreference
import io.wax911.support.custom.recycler.event.SupportScrollListener
import io.wax911.support.util.SupportCoroutineUtil
import org.greenrobot.eventbus.EventBus

abstract class SupportPresenter<S : SupportPreference>(protected var context: Context?) :
        SupportScrollListener(), LifecycleListener, SupportCoroutineUtil {

    val bundle by lazy { Bundle() }

    val supportPreference: S? by lazy { createPreference() }

    /**
     * Enables or disables action mode, behaviour should be implemented in your adapter, in
     * the [io.wax911.support.custom.recycler.SupportViewHolder.clickListener].
     * Default value for this property is false
     *
     * @see io.wax911.support.custom.recycler.SupportViewHolder
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

    /**
     * Provides the preference object to the lazy initializer
     *
     * @return instance of the preference class
     */
    protected abstract fun createPreference() : S?

    companion object {

        /**
         * Trigger all subscribers that may be listening. This method makes use of sticky broadcasts
         * in case all subscribed listeners were not loaded in time for the broadcast
         *
         * @param param the object of type T to send
         * @param isSticky set true to make sticky post
         */
        fun <T> notifyAllListeners(param : T, isSticky: Boolean = false) = when {
            isSticky -> EventBus.getDefault().postSticky(param)
            else -> EventBus.getDefault().post(param)
        }
    }
}
