package io.wax911.support.custom.activity

import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.annotation.ColorRes
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import io.wax911.support.R
import io.wax911.support.base.view.CompatView
import io.wax911.support.custom.fragment.SupportFragment
import io.wax911.support.custom.presenter.SupportPresenter
import io.wax911.support.custom.viewmodel.SupportViewModel
import org.greenrobot.eventbus.EventBus
import retrofit2.Call

abstract class SupportActivity<M, P : SupportPresenter<*>>: AppCompatActivity(), CompatView<M> {

    private var isClosing: Boolean = false

    protected var id: Long = -1
    protected var offScreenLimit = 3
    protected var disableNavigationStyle: Boolean = false

    protected var actionBar: ActionBar? = null
    protected var viewModel: SupportViewModel<M, *>? = null
    protected var supportFragment : SupportFragment<*, *, *>? = null

    protected lateinit var presenter: P

    /**
     * Some activities may have custom themes and if that's the case
     * override this method and set your own theme style, also if you wish
     * to apply the default navigation bar style for light themes
     */
    protected fun configureActivity() {
        initPresenter()
        setThemeStyle()
    }

    /**
     * Changes the navigation bar color depending on the selected theme
     */
    private fun setNavigationStyle() = when (!disableNavigationStyle ) {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ->
            if (presenter.supportPreference.getTheme() == R.style.SupportThemeLight)
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        else
            window.clearFlags(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR)
        else -> { }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        configureActivity()
        super.onCreate(savedInstanceState)
        actionBar = supportActionBar
    }

    override fun setSupportActionBar(toolbar: Toolbar?) {
        super.setSupportActionBar(toolbar)
        actionBar?.setDisplayHomeAsUpEnabled(true)
    }

    /**
     * Changes the navigation bar color and theme style depending on the selected theme
     * @see SupportActivity#setNavigationStyle()
     */
    private fun setThemeStyle() {
        setTheme(presenter.supportPreference.getTheme())
        setNavigationStyle()
    }

    fun disableToolbarTitle() = actionBar?.setDisplayShowTitleEnabled(false)

    protected fun setTransparentStatusBar() {
        when {
            Build.VERSION.SDK_INT >= 21 -> {
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                window.statusBarColor = ContextCompat.getColor(this, android.R.color.transparent)
            }
        }
    }

    protected fun setTransparentStatusBarWithColor(@ColorRes color: Int) {
        if (Build.VERSION.SDK_INT >= 21) {
            val colorInt = ContextCompat.getColor(this, color)
            window.statusBarColor = colorInt
            window.navigationBarColor = colorInt
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            super.onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    /**
     * Check if the current fragment activity has a permission granted to it.
     * If no permission is granted then this method will request a permission for you
     * @see ActivityCompat#requestPermissions
     */
    override fun requestPermissionIfMissing(manifestPermission: String): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return true
        else if (ContextCompat.checkSelfPermission(this, manifestPermission) == PackageManager.PERMISSION_GRANTED)
            return true
        else if (!ActivityCompat.shouldShowRequestPermissionRationale(this, manifestPermission))
            ActivityCompat.requestPermissions(this, arrayOf(manifestPermission), compatViewPermissionKey)
        return false
    }

    /**
     * Dispatch onPause() to fragments.
     */
    override fun onPause() {
        super.onPause()
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this)
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }

    /**
     * Take care of popping the fragment back stack or finishing the activity
     * as appropriate.
     */
    override fun onBackPressed() {
        if (supportFragment != null )
            if (supportFragment!!.hasBackPressableAction())
                return
        return super.onBackPressed()
    }

    protected fun isAtLeastState(state: Lifecycle.State): Boolean =
            lifecycle.currentState.isAtLeast(state)

    /**
     * Called when the data is changed.
     * @param data The new data
     */
    override fun onChanged(data: M) {
        Log.i(getViewName(), "onChanged() from view mutableLiveData has received data")
    }

    override fun onResponseError(call: Call<M>, throwable: Throwable) {
        Log.e(getViewName(), "onResponseError", throwable)
    }

    override fun onResponseSuccess(call: Call<M>, message: String) {
        Log.i(getViewName(), message)
    }

    override fun onSharedPreferenceChanged(preference: SharedPreferences?, key: String?) {
        if (!TextUtils.isEmpty(key))
            Log.d(getViewName(), "onSharedPreferenceChanged -> $key | Changed value")
    }
}
