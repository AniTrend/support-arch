package io.wax911.support.util

import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar
import io.wax911.support.R
import io.wax911.support.getColorFromAttr

object SupportNotifyUtil {

    fun make(parent: View, stringRes: String, duration: Int): Snackbar {
        val snackBar = Snackbar.make(parent, stringRes, duration)
        snackBar.view.setBackgroundColor(parent.context.getColorFromAttr(R.attr.colorPrimaryDark))
        val mainTextView = snackBar.view.findViewById<TextView>(R.id.snackbar_text)
        val actionTextView = snackBar.view.findViewById<TextView>(R.id.snackbar_action)
        mainTextView.setTextColor(parent.context.getColorFromAttr(R.attr.titleColor))
        actionTextView.setTextColor(parent.context.getColorFromAttr(R.attr.colorAccent))
        actionTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
        return snackBar
    }

    fun make(parent: View, @StringRes stringRes: Int, duration: Int): Snackbar =
            make(parent, parent.context.getString(stringRes), duration)
}