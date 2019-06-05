package io.wax911.sample.data.model.attribute

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class Genre(
    @PrimaryKey
    val name: String,
    val slug: String
): Parcelable