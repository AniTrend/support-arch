package io.wax911.sample.data.model.container

import com.google.gson.annotations.SerializedName

data class Anticipated<M>(
    val list_count: Int,
    @SerializedName(
        value = "show",
        alternate = ["movie"]
    )
    val result: M
)