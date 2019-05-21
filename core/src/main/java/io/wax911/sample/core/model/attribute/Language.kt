package io.wax911.sample.core.model.attribute

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
data class Language(
    @PrimaryKey
    val name: String,
    val code: String
): Parcelable