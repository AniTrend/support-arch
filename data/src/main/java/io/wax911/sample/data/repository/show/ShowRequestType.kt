package io.wax911.sample.data.repository.show

import androidx.annotation.StringDef

@StringDef(
    ShowRequestType.SHOW_TYPE_POPULAR,
    ShowRequestType.SHOW_TYPE_TRENDING,
    ShowRequestType.SHOW_TYPE_ANTICIPATED
)
annotation class ShowRequestType {
    companion object {
        const val SHOW_TYPE_POPULAR = "SHOW_TYPE_POPULAR"
        const val SHOW_TYPE_TRENDING = "SHOW_TYPE_TRENDING"
        const val SHOW_TYPE_ANTICIPATED = "SHOW_TYPE_ANTICIPATED"
    }
}