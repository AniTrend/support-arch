package io.wax911.sample.data.model.show.contract

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import io.wax911.sample.data.model.contract.TraktIds
import kotlinx.android.parcel.Parcelize

@Entity(
    indices = [
        Index(value = ["tvdb"], unique = true),
        Index(value = ["slug"], unique = true),
        Index(value = ["imdb"], unique = true)
    ]
)
@Parcelize
data class ShowIds(
    val tvdb: Int,
    val tvrage: Int,
    @PrimaryKey
    override val trakt: Int,
    override val slug: String,
    override val imdb: String?,
    override val tmdb: Int?
): TraktIds, Parcelable