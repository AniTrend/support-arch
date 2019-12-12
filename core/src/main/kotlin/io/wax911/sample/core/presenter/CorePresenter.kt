package io.wax911.sample.core.presenter

import android.content.Context
import co.anitrend.arch.core.presenter.SupportPresenter
import io.wax911.sample.data.util.Settings

open class CorePresenter(
    context: Context,
    settings: Settings
): SupportPresenter<Settings>(context, settings)
