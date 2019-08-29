package io.wax911.sample.data.entitiy.trending

import androidx.room.*
import io.wax911.sample.data.entitiy.contract.IEntryRelation
import io.wax911.sample.data.entitiy.trending.contract.TraktWatcher
import io.wax911.sample.data.model.movie.Movie

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

    @DatabaseView("""
        select tm.*, m.id
        from TrendingMovie tm
        inner join Movie m on (tm.movieId = m.id)
        order by tm.watchers desc
    """)
    data class MovieRelation(
        val id: Int,
        @Embedded
        override val entry: TrendingMovie,
        @Relation(
            parentColumn = "movieId",
            entityColumn = "id"
        )
        override val reference: List<Movie>
    ): IEntryRelation<TrendingMovie, Movie>
}