package io.wax911.sample.core.data.entitiy.trending

import androidx.room.*
import io.wax911.sample.core.data.entitiy.contract.IEntryRelation
import io.wax911.sample.core.data.entitiy.trending.contract.TraktWatcher
import io.wax911.sample.core.model.show.Show

@Entity(
    indices = [
        Index(value = ["showId"], unique = true)
    ],
    foreignKeys = [
        ForeignKey(
            deferred = true,
            entity = Show::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("showId"),
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
data class TrendingShow(
    val showId: Int,
    @PrimaryKey(autoGenerate = true)
    override val id: Int,
    override val watchers: Int
): TraktWatcher {

    class ShowRelation: IEntryRelation<TrendingShow, Show> {
        @Embedded
        override var entry: TrendingShow? = null

        @Relation(
            parentColumn = "showId",
            entityColumn = "id"
        )
        override var reference: List<Show> = emptyList()
    }
}