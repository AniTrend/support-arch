package io.wax911.sample.view.activity.base

import android.os.Bundle
import io.wax911.sample.R
import io.wax911.sample.databinding.ActivitySplashBinding
import io.wax911.sample.presenter.BasePresenter
import io.wax911.sample.view.activity.index.MainActivity
import io.wax911.support.ui.activity.SupportActivity
import io.wax911.support.extension.startNewActivity
import io.wax911.support.extension.util.SupportDateUtil

class SplashActivity : SupportActivity<Nothing, BasePresenter>() {

    private var binding: ActivitySplashBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
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
        val description = getString(R.string.app_splash_description, SupportDateUtil.getCurrentYear(0))
        binding?.splashDescription?.text = description
        makeRequest()
    }

    override fun initPresenter() = BasePresenter.newInstance(this)

    override fun updateUI() {
        startNewActivity<MainActivity>(intent.extras)
        finish()
    }

    override fun makeRequest() {
        updateUI()
    }
}
