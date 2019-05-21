package io.wax911.sample.core.model.movie.contract

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import io.wax911.sample.core.model.contract.TraktIds
import kotlinx.android.parcel.Parcelize

@Entity(
    indices = [
        Index(value = ["slug"], unique = true),
        Index(value = ["imdb"], unique = true)
    ]
)
@Parcelize
data class MovieIds(
    @PrimaryKey
    override val trakt: Int,
    override val slug: String,
    override val imdb: String,
    override val tmdb: Int
): TraktIds, Parcelable