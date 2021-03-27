package co.anitrend.arch.ui.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import co.anitrend.arch.extension.coroutine.ISupportCoroutine
import co.anitrend.arch.ui.common.ILifecycleController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope

/**
 * Core implementation for [androidx.appcompat.app.AppCompatActivity] components
 *
 * @since v0.9.X
 *
 * @see ISupportCoroutine
 * @see ISupportFragment
 */
abstract class SupportActivity : AppCompatActivity(),
    ILifecycleController, CoroutineScope by MainScope() {

    /** Current fragment in view tag which will be used by [getSupportFragmentManager] */
    protected var currentFragmentTag: String? = null

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
}
