package io.wax911.sample.data.model.show.meta

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Airs(
    val day: String,
    val time: String,
    val timezone: String
): Parcelable