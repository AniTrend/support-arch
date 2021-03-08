package co.anitrend.arch.core.provider

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

/**
 * FileProvider is a special subclass of [android.content.ContentProvider] that facilitates secure sharing
 * of files associated with an app by creating a `content://` [android.net.Uri] for a file
 * instead of a `file:///` [android.net.Uri]
 *
 * @since v1.2.x
 * @see FileProvider
 */
open class SupportFileProvider : FileProvider() {

    companion object {
        /**
         * Proxy for [FileProvider.getUriForFile]
         *
         * @param context [Context] for the current component.
         * @param authority The authority of a [FileProvider] defined in a `<provider>`
         * element in your app's manifest.
         * @param file [File] pointing to the filename for which you want a `content` [Uri].
         *
         * @return A content URI for the file.
         * @throws IllegalArgumentException When the given [file] is outside the paths supported
         * by the provider.
         */
        @Throws(IllegalArgumentException::class)
        fun uriForFile(context: Context, authority: String, file: File): Uri {
            return getUriForFile(context, authority, file)
        }
    }
}