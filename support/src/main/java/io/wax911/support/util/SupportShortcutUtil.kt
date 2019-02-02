package io.wax911.support.util

import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.content.pm.ShortcutManager
import android.os.Build
import android.os.Bundle

import androidx.annotation.RequiresApi

@TargetApi(Build.VERSION_CODES.N_MR1)
@RequiresApi(Build.VERSION_CODES.N_MR1)
abstract class SupportShortcutUtil private constructor(protected val context: Context) {

    protected val shortcutManager: ShortcutManager by lazy {
        context.getSystemService(ShortcutManager::class.java)
    }

    protected fun <S> createIntentAction(targetActivity: Class<S>, param: Bundle): Intent {
        return Intent(context, targetActivity).apply {
            putExtras(param)
            action = Intent.ACTION_VIEW
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
    }

    abstract fun createShortcuts(vararg shortcutBuilders : SupportShortcutBuilder)

    abstract fun disableShortcuts(vararg shortcuts: Int)

    abstract fun enableShortcuts(vararg shortcuts: Int)

    abstract fun reportShortcutUsage(shortcut: Int)

    /** Shortcut Builder Helper Class  */
    interface SupportShortcutBuilder {

        var shortcutType: Int
        var params: Bundle

        fun build(): SupportShortcutBuilder = this

        fun setShortcutType(shortcutType: Int) = apply {
            this.shortcutType = shortcutType
        }

        fun setShortcutParams(params: Bundle) = apply {
            this.params = params
        }
    }
}
