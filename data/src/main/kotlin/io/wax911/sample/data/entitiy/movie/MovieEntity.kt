package io.wax911.sample.data.entitiy.movie

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import io.wax911.sample.data.entitiy.movie.contract.MovieEntityIds
import io.wax911.sample.domain.entities.contract.TraktEntity
import io.wax911.sample.domain.entities.contract.TraktRankEntity

@Entity
data class MovieEntity(
    @PrimaryKey(autoGenerate = true)
    override val id: Int = 0,
    @Embedded
    val ids: MovieEntityIds,
    val tagline: String?,
    val released: String?,
    override val title: String?,
    override val year: Int?,
    override val overview: String?,
    override val runtime: Int?,
    override val country: String?,
    override val trailer: String?,
    override val homepage: String?,
    override val rating: Double?,
    override val votes: Int?,
    override val updatedAt: String?,
    override val language: String?,
    override val genres: List<String>?,
    override val certification: String?,
    override val trendingRank: Int?
): TraktEntity, TraktRankEntity {

    override fun equals(other: Any?): Boolean {
        return when (other) {
            is TraktEntity -> other.id == id
            else -> super.equals(other)
        }
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + ids.hashCode()
        result = 31 * result + tagline.hashCode()
        result = 31 * result + (released?.hashCode() ?: 0)
        result = 31 * result + title.hashCode()
        result = 31 * result + (year?.hashCode() ?: 0)
        result = 31 * result + (overview?.hashCode() ?: 0)
        result = 31 * result + (runtime ?: 0)
        result = 31 * result + (country?.hashCode() ?: 0)
        result = 31 * result + (trailer?.hashCode() ?: 0)
        result = 31 * result + (homepage?.hashCode() ?: 0)
        result = 31 * result + (rating?.hashCode() ?: 0)
        result = 31 * result + (votes ?: 0)
        result = 31 * result + (language?.hashCode() ?: 0)
        result = 31 * result + (genres?.hashCode() ?: 0)
        result = 31 * result + (certification?.hashCode() ?: 0)
        result = 31 * result + (trendingRank.hashCode())
        return result
    }
}