package io.wax911.sample.data.model.attribute

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(
    indices = [
        Index(value = ["code"], unique = true)
    ]
)
@Parcelize
data class Country(
    @PrimaryKey
    val name: String,
    val code: String
): Parcelable