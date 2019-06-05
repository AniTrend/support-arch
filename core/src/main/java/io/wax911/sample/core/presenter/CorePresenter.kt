package io.wax911.sample.core.presenter

import android.content.Context
import io.wax911.sample.data.util.Settings
import io.wax911.support.core.presenter.SupportPresenter

class CorePresenter(
    context: Context?,
    settings: Settings
): SupportPresenter<Settings>(context, settings)
