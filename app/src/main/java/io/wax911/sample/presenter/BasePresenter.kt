package io.wax911.sample.presenter

import android.content.Context

import io.wax911.support.custom.presenter.SupportPresenter
import io.wax911.sample.util.Settings

class BasePresenter(context: Context): SupportPresenter<Settings>(context) {

    init { supportPreference = Settings(context) }

}
