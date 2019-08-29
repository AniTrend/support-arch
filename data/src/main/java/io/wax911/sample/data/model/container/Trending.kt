package io.wax911.sample.data.model.container

import com.google.gson.annotations.SerializedName

data class Trending<M>(
    val watchers: Int,
    @SerializedName(
        value = "show",
        alternate = ["movie"]
    )
    val result: M
)