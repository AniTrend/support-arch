package io.wax911.sample.core.api.endpoint

import io.wax911.sample.core.model.container.Aniticipated
import io.wax911.sample.core.model.container.Trending
import io.wax911.sample.core.model.meta.ResourceType
import io.wax911.sample.core.model.show.Show
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ShowEndpoints {

    @GET("/shows/trending")
    fun getTrendingShows(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("extended")
        @ResourceType resourceType: String = ResourceType.FULL
    ): Call<List<Trending<Show>>>

    @GET("/shows/popular")
    fun getPopularShows(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("extended")
        @ResourceType resourceType: String = ResourceType.FULL
    ): Call<List<Show>>

    @GET("/shows/anticipated")
    fun getAniticipatedShows(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("extended")
        @ResourceType resourceType: String = ResourceType.FULL
    ): Call<List<Aniticipated<Show>>>
}