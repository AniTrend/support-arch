package io.wax911.sample.data.model.meta

import androidx.annotation.StringDef

@StringDef(
    MediaCategoryContract.MOVIES,
    MediaCategoryContract.SHOWS
)
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.TYPEALIAS, AnnotationTarget.CLASS)
annotation class MediaCategoryContract {
    companion object {
        const val MOVIES = "movies"
        const val SHOWS = "shows"

        const val MEDIA_CATEGORY_TYPE = "MEDIA_CATEGORY_TYPE"
    }
}

@MediaCategoryContract
typealias MediaCategory = String