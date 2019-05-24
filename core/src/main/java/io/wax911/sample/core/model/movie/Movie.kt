package io.wax911.sample.core.model.movie

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import io.wax911.sample.core.model.contract.TraktEntity
import io.wax911.sample.core.model.movie.contract.MovieIds
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Entity
@Parcelize
data class Movie(
    @PrimaryKey(autoGenerate = true)
    override var id: Int,
    val ids: @RawValue MovieIds,
    val tagline: String,
    val released: String?,
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
): TraktEntity, Parcelable