package io.wax911.support.custom.presenter

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import io.wax911.support.base.event.LifecycleListener
import io.wax911.support.custom.preference.SupportPreference
import io.wax911.support.custom.recycler.RecyclerScrollListener
import org.greenrobot.eventbus.EventBus

abstract class SupportPresenter<S : SupportPreference>(protected var context: Context) : RecyclerScrollListener(), LifecycleListener {

    val bundle by lazy { Bundle() }


    lateinit var supportPreference: S

    /**
     * Trigger all subscribers that may be listening. This method makes use of sticky broadcasts
     * in case all subscribed listeners were not loaded in time for the broadcast
     * <br/>
     *
     * @param param the object of type T to send
     * @param isSticky set true to make sticky post
     */
    fun <T> notifyAllListeners(param : T, isSticky: Boolean) =
        if (isSticky) EventBus.getDefault().postSticky(param)
        else EventBus.getDefault().post(param)

    /**
     * Unregister any listeners from fragments or activities
     */
    override fun onPause(changeListener: SharedPreferences.OnSharedPreferenceChangeListener) =
            supportPreference.sharedPreferences.unregisterOnSharedPreferenceChangeListener(changeListener)

    /**
     * Register any listeners from fragments or activities
     */
    override fun onResume(changeListener: SharedPreferences.OnSharedPreferenceChangeListener) =
            supportPreference.sharedPreferences.registerOnSharedPreferenceChangeListener(changeListener)

    /**
     * Destroy any reference which maybe attached to
     * our context
     */
    override fun onDestroy() {

    }
}
