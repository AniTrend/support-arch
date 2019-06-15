package io.wax911.sample.data.usecase.media

import androidx.annotation.StringDef

@StringDef(
    MediaRequestType.MEDIA_TYPE_POPULAR,
    MediaRequestType.MEDIA_TYPE_TRENDING,
    MediaRequestType.MEDIA_TYPE_ANTICIPATED
)
annotation class MediaRequestType {
    companion object {
        const val MEDIA_TYPE_POPULAR = "MEDIA_TYPE_POPULAR"
        const val MEDIA_TYPE_TRENDING = "MEDIA_TYPE_TRENDING"
        const val MEDIA_TYPE_ANTICIPATED = "MEDIA_TYPE_ANTICIPATED"

        const val selectedMediaType = "requestType"
    }
}