package io.wax911.sample.core.model.show

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import io.wax911.sample.core.model.contract.TraktEntity
import io.wax911.sample.core.model.show.contract.ShowIds
import io.wax911.sample.core.model.show.meta.Airs
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Entity
@Parcelize
data class Show(
    @PrimaryKey(autoGenerate = true)
    override var id: Int,
    val ids: @RawValue ShowIds,
    val first_aired: String?,
    val airs: Airs?,
    val network: String?,
    val status: String,
    val aired_episodes: Int?,
    override val title: String,
    override val year: Int,
    override val overview: String?,
    override val runtime: Int?,
    override val country: String?,
    override val trailer: String?,
    override val homepage: String?,
    override val rating: Double?,
    override val votes: Int?,
    override val updated_at: String?,
    override val language: String?,
    override val genres: List<String>?,
    override val certification: String?
): TraktEntity, Parcelable {

    override fun equals(other: Any?): Boolean {
        return when (other) {
            is TraktEntity -> other.id == id
            else -> super.equals(other)
        }
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + ids.hashCode()
        result = 31 * result + (first_aired?.hashCode() ?: 0)
        result = 31 * result + (airs?.hashCode() ?: 0)
        result = 31 * result + (network?.hashCode() ?: 0)
        result = 31 * result + status.hashCode()
        result = 31 * result + (aired_episodes ?: 0)
        result = 31 * result + title.hashCode()
        result = 31 * result + year
        result = 31 * result + (overview?.hashCode() ?: 0)
        result = 31 * result + (runtime ?: 0)
        result = 31 * result + (country?.hashCode() ?: 0)
        result = 31 * result + (trailer?.hashCode() ?: 0)
        result = 31 * result + (homepage?.hashCode() ?: 0)
        result = 31 * result + (rating?.hashCode() ?: 0)
        result = 31 * result + (votes ?: 0)
        result = 31 * result + (updated_at?.hashCode() ?: 0)
        result = 31 * result + (language?.hashCode() ?: 0)
        result = 31 * result + (genres?.hashCode() ?: 0)
        result = 31 * result + (certification?.hashCode() ?: 0)
        return result
    }
}