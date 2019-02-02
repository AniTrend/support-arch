package io.wax911.sample.view.activity.base

import android.os.Bundle
import android.os.Handler
import io.wax911.sample.R
import io.wax911.sample.model.BaseModel
import io.wax911.sample.presenter.BasePresenter
import io.wax911.sample.view.activity.index.MainActivity
import io.wax911.sample.viewmodel.BaseViewModel
import io.wax911.support.custom.activity.SupportActivity
import io.wax911.support.custom.viewmodel.SupportViewModel
import io.wax911.support.startNewActivity
import io.wax911.support.util.SupportDateUtil
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : SupportActivity<BaseModel, BasePresenter>() {

    override fun initPresenter(): BasePresenter = BasePresenter.newInstance(this)

    override fun initViewModel(): SupportViewModel<BaseModel?, *>? =
            BaseViewModel.newInstance(this, this)

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
        Handler().postDelayed({ this.updateUI() }, 2000)
    }
}
