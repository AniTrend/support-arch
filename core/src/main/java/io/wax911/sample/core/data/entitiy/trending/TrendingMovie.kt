package io.wax911.sample.core.data.entitiy.trending

import androidx.room.*
import io.wax911.sample.core.data.entitiy.contract.IEntryRelation
import io.wax911.sample.core.data.entitiy.trending.contract.TraktWatcher
import io.wax911.sample.core.model.movie.Movie

@Entity(
    indices = [
        Index(value = ["movieId"], unique = true)
    ],
    foreignKeys = [
        ForeignKey(
            deferred = true,
            entity = Movie::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("movieId"),
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
data class TrendingMovie(
    val movieId: Int,
    @PrimaryKey(autoGenerate = true)
    override val id: Int,
    override val watchers: Int
): TraktWatcher {

    class MovieRelation: IEntryRelation<TrendingMovie, Movie> {
        @Embedded
        override var entry: TrendingMovie? = null

        @Relation(
            parentColumn = "movieId",
            entityColumn = "id"
        )
        override var reference: List<Movie> = emptyList()
    }
}