package io.wax911.sample.data.api.endpoint

import io.wax911.sample.data.api.endpoint.contract.TraktEndpointFactory
import io.wax911.sample.data.model.container.Anticipated
import io.wax911.sample.data.model.container.Trending
import io.wax911.sample.data.model.meta.ResourceType
import io.wax911.sample.data.model.movie.Movie
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieEndpoint {

    @GET("/movies/trending")
    suspend fun getTrendingMovies(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("extended")
        @ResourceType resourceType: String = ResourceType.FULL
    ): Response<List<Trending<Movie>>>

    @GET("/movies/popular")
    suspend fun getPopularMovies(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("extended")
        @ResourceType resourceType: String = ResourceType.FULL
    ): Response<List<Movie>>

    @GET("/movies/anticipated")
    suspend fun getAniticipatedMovies(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("extended")
        @ResourceType resourceType: String = ResourceType.FULL
    ): Response<List<Anticipated<Movie>>>


    companion object : TraktEndpointFactory<MovieEndpoint>(
        endpoint = MovieEndpoint::class
    )
}