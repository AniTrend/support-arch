package io.wax911.sample.data.model.meta

import androidx.annotation.StringDef

@StringDef(
    MediaCategory.MOVIES, MediaCategory.SHOWS
)
@Retention(AnnotationRetention.SOURCE)
annotation class MediaCategory {
    companion object {
        const val MOVIES = "movies"
        const val SHOWS = "shows"
    }
}