package io.wax911.sample.view.activity.index

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import io.wax911.sample.R
import io.wax911.sample.presenter.BasePresenter
import io.wax911.sample.util.StateUtil
import io.wax911.support.custom.activity.SupportActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : SupportActivity<Nothing, BasePresenter>(), NavigationView.OnNavigationItemSelectedListener,
        BottomNavigationView.OnNavigationItemSelectedListener  {

    private lateinit var mDrawerToggle: ActionBarDrawerToggle

    @IdRes
    private var selectedItem: Int = 0
    @StringRes
    private var selectedTitle: Int = 0

    private val navigationListener = { item: MenuItem ->
        drawerLayout.closeDrawer(GravityCompat.START)
        when (item.itemId) {
            R.id.nav_theme -> {
                presenter.supportPreference.toggleTheme()
                recreate()
            }
            R.id.nav_about -> Toast.makeText(this@MainActivity, "About", Toast.LENGTH_SHORT).show()
            R.id.nav_contact -> Toast.makeText(this@MainActivity, "Contact", Toast.LENGTH_SHORT).show()
        }
        false
    }

    override fun initPresenter() {
        presenter = BasePresenter.newInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(supportToolbar)
        mDrawerToggle = ActionBarDrawerToggle(this, drawerLayout, supportToolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close)

        if (intent.hasExtra(StateUtil.arg_redirect))
            selectedItem = intent.getIntExtra(StateUtil.arg_redirect, 0)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        bottomNavigation.setOnNavigationItemSelectedListener(this)
        navigationDrawer.setNavigationItemSelectedListener(navigationListener)
        updateUI()
    }

    override fun onPause() {
        super.onPause()
        drawerLayout.removeDrawerListener(mDrawerToggle)
    }

    override fun onResume() {
        super.onResume()
        drawerLayout.addDrawerListener(mDrawerToggle)
        mDrawerToggle.syncState()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(StateUtil.key_navigation_selected, selectedItem)
        outState.putInt(StateUtil.key_navigation_title, selectedTitle)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        if (savedInstanceState != null) {
            selectedItem = savedInstanceState.getInt(StateUtil.key_navigation_selected)
            selectedTitle = savedInstanceState.getInt(StateUtil.key_navigation_title)
        }
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
            return
        }
        super.onBackPressed()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_activity, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_search ->
                //mFlipper.showNext();
                return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        if (selectedItem != item.itemId) {
            selectedItem = item.itemId
            onNavigate(selectedItem)
        }
        return true
    }

    private fun onNavigate(@IdRes menu: Int) {
        when (menu) {
            R.id.nav_home -> {
                selectedTitle = R.string.nav_home
                // supportFragment = FragmentHome.newInstance()
            }
            R.id.nav_history -> {
                selectedTitle = R.string.nav_history
                // supportFragment = FragmentHistory.newInstance()
            }
        }

        if (supportFragment != null) {
            supportToolbar.setTitle(selectedTitle)
            val fragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.contentFrame, supportFragment!!, supportFragment!!.tag)
            fragmentTransaction.commit()
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
