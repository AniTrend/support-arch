package io.wax911.sample.core.presenter

import android.content.Context
import io.wax911.sample.core.util.Settings
import io.wax911.sample.core.util.StateUtil
import io.wax911.support.core.presenter.SupportPresenter
import io.wax911.support.core.factory.InstanceCreator

class CorePresenter(
    context: Context?,
    settings: Settings
): SupportPresenter<Settings>(context, settings) {

    /**
     * Provides pagination size for calculating offsets
     */
    override fun paginationSize() = StateUtil.pagingLimit
}
