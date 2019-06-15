package io.wax911.sample.data.api.endpoint

import io.wax911.sample.data.model.container.Anticipated
import io.wax911.sample.data.model.container.Trending
import io.wax911.sample.data.model.meta.ResourceType
import io.wax911.sample.data.model.show.Show
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ShowEndpoint {

    @GET("/shows/trending")
    suspend fun getTrendingShows(
        @Query("page") page: Int?,
        @Query("limit") limit: Int?,
        @Query("extended")
        @ResourceType resourceType: String = ResourceType.FULL
    ): Response<List<Trending<Show>>>

    @GET("/shows/popular")
    suspend fun getPopularShows(
        @Query("page") page: Int?,
        @Query("limit") limit: Int?,
        @Query("extended")
        @ResourceType resourceType: String = ResourceType.FULL
    ): Response<List<Show>>

    @GET("/shows/anticipated")
    suspend fun getAnticipatedShows(
        @Query("page") page: Int?,
        @Query("limit") limit: Int?,
        @Query("extended")
        @ResourceType resourceType: String = ResourceType.FULL
    ): Response<List<Anticipated<Show>>>
}