package io.wax911.sample.view.activity.base

import android.os.Bundle
import io.wax911.sample.R
import io.wax911.sample.core.presenter.CorePresenter
import io.wax911.sample.core.view.TraktTrendActivity
import io.wax911.sample.databinding.ActivitySplashBinding
import io.wax911.sample.view.activity.index.MainActivity
import io.wax911.support.extension.startNewActivity
import io.wax911.support.extension.util.SupportDateUtil
import io.wax911.support.ui.activity.SupportActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject

class SplashActivity : TraktTrendActivity<Nothing, CorePresenter>() {

    private val binding: ActivitySplashBinding?
        get() = ActivitySplashBinding.inflate(layoutInflater)

    /**
     * Should be created lazily through injection or lazy delegate
     *
     * @return supportPresenter of the generic type specified
     */
    override val supportPresenter: CorePresenter by inject()

    /**
     * Additional initialization to be done in this method, if the overriding class is type of [SupportFragment]
     * then this method will be called in [SupportFragment.onCreate]. Otherwise [SupportActivity.onPostCreate]
     * invokes this function
     *
     * @see [SupportActivity.onPostCreate] and [SupportFragment.onCreate]
     * @param
     */
    override fun initializeComponents(savedInstanceState: Bundle?) {
        val description = getString(
            R.string.app_splash_description,
            SupportDateUtil.getCurrentYear(0)
        )
        binding?.splashDescription?.text = description
        makeRequest()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding?.root)
    }

    override fun updateUI() {
        launch {
            delay(1000)
            withContext(Dispatchers.Main) {
                startNewActivity<MainActivity>(intent.extras)
                finish()
            }
        }

    }

    override fun makeRequest() {
        updateUI()
    }
}
