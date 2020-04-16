package co.anitrend.arch.ui.activity

import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import co.anitrend.arch.core.presenter.SupportPresenter
import co.anitrend.arch.extension.coroutine.SupportCoroutine
import co.anitrend.arch.ui.view.contract.ISupportFragmentActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import timber.log.Timber

/**
 * Core implementation for [androidx.appcompat.app.AppCompatActivity] components
 *
 * @since v0.9.X
 * @see SupportCoroutine
 * @see ISupportFragmentActivity
 */
abstract class SupportActivity<M, P : SupportPresenter<*>>: AppCompatActivity(),
    ISupportFragmentActivity<M, P>, CoroutineScope by MainScope() {

    protected val moduleTag: String = javaClass.simpleName

    private var isClosing: Boolean = false

    protected var supportFragmentActivity : ISupportFragmentActivity<*, *>? = null

    /**
     * Can be used to configure custom theme styling as desired
     */
    protected abstract fun configureActivity()

    override fun onCreate(savedInstanceState: Bundle?) {
        configureActivity()
        super.onCreate(savedInstanceState)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        initializeComponents(savedInstanceState)
    }

    override fun setSupportActionBar(toolbar: Toolbar?) {
        super.setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    /**
     * Check if the current fragment activity has a permission granted to it.
     * If no permission is granted then this method will request a permission for you
     * @see ActivityCompat.requestPermissions
     */
    override fun requestPermissionIfMissing(manifestPermission: String): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return true
        else if (ContextCompat.checkSelfPermission(this, manifestPermission) == PackageManager.PERMISSION_GRANTED)
            return true
        else if (!ActivityCompat.shouldShowRequestPermissionRationale(this, manifestPermission))
            ActivityCompat.requestPermissions(this, arrayOf(manifestPermission), compatViewPermissionValue)
        return false
    }

    /**
     * Take care of popping the fragment back stack or finishing the activity
     * as appropriate.
     */
    override fun onBackPressed() {
        if (supportFragmentActivity?.hasBackPressableAction() == true)
            return
        return super.onBackPressed()
    }

    override fun onSharedPreferenceChanged(preference: SharedPreferences?, key: String?) {
        if (!key.isNullOrEmpty())
            Timber.tag(moduleTag).d("onSharedPreferenceChanged -> $key | Changed value")
    }
}
