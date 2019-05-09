package io.wax911.sample.core.data.entitiy.meta

import androidx.annotation.StringDef


@StringDef(
    TraktMediaCategory.SHOW,
    TraktMediaCategory.MOVIE
)
annotation class TraktMediaCategory {
    companion object {
        const val SHOW = "show"
        const val MOVIE = "show"
    }
}