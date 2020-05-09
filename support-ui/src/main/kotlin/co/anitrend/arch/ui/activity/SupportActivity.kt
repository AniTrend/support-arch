package co.anitrend.arch.ui.activity

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import co.anitrend.arch.extension.coroutine.SupportCoroutine
import co.anitrend.arch.ui.activity.contract.ISupportActivity
import co.anitrend.arch.ui.common.ISupportActionUp
import co.anitrend.arch.ui.fragment.contract.ISupportFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope

/**
 * Core implementation for [androidx.appcompat.app.AppCompatActivity] components
 *
 * @since v0.9.X
 *
 * @see SupportCoroutine
 * @see ISupportFragment
 */
abstract class SupportActivity : AppCompatActivity(),
    ISupportActivity, CoroutineScope by MainScope() {

    override val moduleTag = javaClass.simpleName

    /** Current fragment in view tag which will be used by [getSupportFragmentManager] */
    protected var currentFragmentTag: String? = null

    /**
     * Can be used to configure custom theme styling as desired
     */
    protected abstract fun configureActivity()

    /**
     * Checks if the current loaded fragment is allowed to intercept action.
     *
     * @see ISupportActionUp
     */
    protected open fun currentFragmentInterceptsActionUp(): Boolean {
        val fragment = supportFragmentManager
                .findFragmentByTag(currentFragmentTag)
        return when (fragment) {
            is ISupportActionUp -> fragment.hasBackPressableAction()
            else -> false
        }
    }

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

    override fun onBackPressed() {
        if (currentFragmentInterceptsActionUp())
            return
        return super.onBackPressed()
    }
}
