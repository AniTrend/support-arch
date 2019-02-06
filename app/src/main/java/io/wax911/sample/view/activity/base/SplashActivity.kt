package io.wax911.sample.view.activity.base

import android.os.Bundle
import io.wax911.sample.R
import io.wax911.sample.presenter.BasePresenter
import io.wax911.sample.view.activity.index.MainActivity
import io.wax911.support.activity.SupportActivity
import io.wax911.support.extension.startNewActivity
import io.wax911.support.util.SupportDateUtil
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : SupportActivity<Nothing, BasePresenter>() {

    override fun initPresenter() = BasePresenter.newInstance(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        val description = getString(R.string.app_splash_description, SupportDateUtil.getCurrentYear(0))
        splash_description.text = description
        makeRequest()
    }

    override fun updateUI() {
        startNewActivity<MainActivity>(intent.extras)
        finish()
    }

    override fun makeRequest() {
        launch {
            delay(2500)
            updateUI()
        }
    }
}
