package io.wax911.sample.data.entitiy.trending

import androidx.room.*
import io.wax911.sample.data.entitiy.contract.IEntryRelation
import io.wax911.sample.data.entitiy.trending.contract.TraktWatcher
import io.wax911.sample.data.model.show.Show

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

    @DatabaseView("""
        select ts.*, m.id
        from TrendingShow ts
        inner join Show m on (ts.showId = m.id)
        order by ts.watchers desc
    """)
    data class ShowRelation(
        val id: Int,
        @Embedded
        override val entry: TrendingShow?,
        @Relation(
            parentColumn = "showId",
            entityColumn = "id"
        )
        override val reference: List<Show>
    ): IEntryRelation<TrendingShow, Show>
}