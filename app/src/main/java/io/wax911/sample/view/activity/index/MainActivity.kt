package io.wax911.sample.view.activity.index

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.navigation.NavigationView
import io.wax911.sample.R
import io.wax911.sample.presenter.BasePresenter
import io.wax911.sample.util.StateUtil
import io.wax911.sample.view.fragment.detail.FragmentHome
import io.wax911.support.ui.activity.SupportActivity
import io.wax911.support.core.util.SupportStateKeyStore
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : SupportActivity<Nothing, BasePresenter>(), NavigationView.OnNavigationItemSelectedListener {

    private val bottomDrawerBehavior: BottomSheetBehavior<FrameLayout> by lazy {
        BottomSheetBehavior.from(bottomNavigationDrawer)
    }

    @IdRes
    private var selectedItem: Int = R.id.nav_home
    @StringRes
    private var selectedTitle: Int = R.string.nav_home

    /**
     * @return the presenter that will be used by the fragment activity
     */
    override fun initPresenter(): BasePresenter = BasePresenter.newInstance(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(bottomAppBar)
        bottomDrawerBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        if (intent.hasExtra(StateUtil.arg_redirect))
            selectedItem = intent.getIntExtra(StateUtil.arg_redirect, R.id.nav_home)
    }

    /**
     * Additional initialization to be done in this method, if the overriding class is type of [SupportFragment]
     * then this method will be called in [SupportFragment.onCreate]. Otherwise [SupportActivity.onPostCreate]
     * invokes this function
     *
     * @see [SupportActivity.onPostCreate] and [SupportFragment.onCreate]
     * @param
     */
    override fun initializeComponents(savedInstanceState: Bundle?) {
        bottomAppBar.apply {
            setNavigationOnClickListener {
                bottomDrawerBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
            }
        }
        floatingShortcutButton.setOnClickListener {
            Toast.makeText(this, "Fab Clicked", Toast.LENGTH_SHORT).show()
        }
        bottomNavigationView.apply {
            setNavigationItemSelectedListener(this@MainActivity)
            setCheckedItem(selectedItem)
        }
        updateUI()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(SupportStateKeyStore.key_navigation_selected, selectedItem)
        outState.putInt(SupportStateKeyStore.key_navigation_title, selectedTitle)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        if (savedInstanceState != null) {
            selectedItem = savedInstanceState.getInt(SupportStateKeyStore.key_navigation_selected)
            selectedTitle = savedInstanceState.getInt(SupportStateKeyStore.key_navigation_title)
        }
    }

    override fun onBackPressed() {
        when (bottomDrawerBehavior.state) {
            BottomSheetBehavior.STATE_EXPANDED,
            BottomSheetBehavior.STATE_HALF_EXPANDED -> {
                bottomDrawerBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                return
            }
            else -> super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_activity, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_search -> {
                Toast.makeText(this, "onMenuItemClick", Toast.LENGTH_SHORT).show()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        if (selectedItem != item.itemId) {
            if (item.groupId != R.id.nav_group_customization) {
                selectedItem = item.itemId
                onNavigate(selectedItem)
            } else
                onNavigate(item.itemId)
        }
        return true
    }

    private fun onNavigate(@IdRes menu: Int) {
        when (menu) {
            R.id.nav_theme -> {
                if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                else
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                recreate()
            }
            R.id.nav_about -> Toast.makeText(this@MainActivity, "About", Toast.LENGTH_SHORT).show()
            R.id.nav_contact -> Toast.makeText(this@MainActivity, "Contact", Toast.LENGTH_SHORT).show()
            R.id.nav_home -> {
                selectedTitle = R.string.nav_home
                supportFragment = FragmentHome.newInstance(intent.extras)
            }
            R.id.nav_history -> {
                selectedTitle = R.string.nav_history
                supportFragment = null
            }
        }

        if (supportFragment != null) {
            bottomAppBar.setTitle(selectedTitle)
            val fragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.contentFrame, supportFragment!!, supportFragment!!.tag)
            fragmentTransaction.commit()
            bottomDrawerBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }
    }

    override fun updateUI() {
        if (selectedItem != 0)
            onNavigate(selectedItem)
        else
            onNavigate(R.id.nav_home)
    }

    override fun makeRequest() {

    }
}
