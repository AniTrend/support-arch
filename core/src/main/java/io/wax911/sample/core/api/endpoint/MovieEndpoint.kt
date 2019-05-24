package io.wax911.sample.core.api.endpoint

import io.wax911.sample.core.model.container.Aniticipated
import io.wax911.sample.core.model.container.Trending
import io.wax911.sample.core.model.meta.ResourceType
import io.wax911.sample.core.model.movie.Movie
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieEndpoint {

    @GET("/movies/trending")
    fun getTrendingMovies(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("extended")
        @ResourceType resourceType: String = ResourceType.FULL
    ): Call<List<Trending<Movie>>>

    @GET("/movies/popular")
    fun getPopularMovies(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("extended")
        @ResourceType resourceType: String = ResourceType.FULL
    ): Call<List<Movie>>

    @GET("/movies/anticipated")
    fun getAniticipatedMovies(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("extended")
        @ResourceType resourceType: String = ResourceType.FULL
    ): Call<List<Aniticipated<Movie>>>
}