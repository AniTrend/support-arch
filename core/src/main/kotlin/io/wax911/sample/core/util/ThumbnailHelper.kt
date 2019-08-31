package io.wax911.sample.core.util

object ThumbnailHelper {

    private const val YOUTUBE_THUMBNAIL = "https://img.youtube.com/vi/%s/hqdefault.jpg"
    private const val NO_THUMBNAIL = "http://placehold.it/1280x720?text=No+Preview+Available"
    private val YOUTUBE_PATTERN = Regex("(\\?v=)(.*)")

    fun getThumbnailFromUrl(url: String?) : String {
        return url?.let {
            var thumbnail = NO_THUMBNAIL
            val matchResult = YOUTUBE_PATTERN.find(url)
            matchResult?.also {
                val videoId = matchResult.groups.last()?.value
                if (videoId != null)
                    thumbnail = String.format(YOUTUBE_THUMBNAIL, videoId)
            }
            thumbnail
        } ?: NO_THUMBNAIL
    }
}