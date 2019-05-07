package io.wax911.sample.core.model.meta

import androidx.annotation.StringDef

@StringDef(
    MetaCategory.MOVIES, MetaCategory.SHOWS
)
@Retention(AnnotationRetention.SOURCE)
annotation class MetaCategory {
    companion object {
        const val MOVIES = "movies"
        const val SHOWS = "shows"
    }
}