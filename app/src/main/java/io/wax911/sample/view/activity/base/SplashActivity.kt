package io.wax911.sample.view.activity.base

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import io.wax911.sample.R
import io.wax911.sample.model.BaseModel
import io.wax911.sample.presenter.BasePresenter
import io.wax911.sample.view.activity.index.MainActivity
import io.wax911.sample.viewmodel.BaseViewModel
import io.wax911.support.custom.activity.SupportActivity
import io.wax911.support.util.SupportDateUtil
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : SupportActivity<BaseModel, BasePresenter>() {

    override fun initPresenter() {
        presenter = BasePresenter.newInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        viewModel = BaseViewModel.newInstance(this, this)
        viewModel!!.observeOn(this, this)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        val description = getString(R.string.app_splash_description, SupportDateUtil.getCurrentYear(0))
        splash_description.text = description
        makeRequest()
    }

    override fun updateUI() {
        startActivity(Intent(this@SplashActivity, MainActivity::class.java))
        finish()
    }

    override fun makeRequest() {
        Handler().postDelayed({ this.updateUI() }, 2000)
    }

    /**
     * Called when the data is changed.
     * @param data The new data
     */
    override fun onChanged(data: BaseModel) {
        super.onChanged(data)
    }
}
