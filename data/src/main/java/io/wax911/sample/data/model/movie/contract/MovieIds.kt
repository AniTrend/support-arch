package io.wax911.sample.data.model.movie.contract

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import io.wax911.sample.data.model.contract.TraktIds

@Entity(
    indices = [
        Index(value = ["slug"], unique = true),
        Index(value = ["imdb"], unique = true),
        Index(value = ["tmdb"], unique = true)
    ]
)
data class MovieIds(
    @PrimaryKey
    override val trakt: Int,
    override val slug: String,
    override val imdb: String?,
    override val tmdb: Int?
): TraktIds