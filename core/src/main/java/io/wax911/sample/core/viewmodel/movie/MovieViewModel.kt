package io.wax911.sample.core.viewmodel.movie

import androidx.paging.PagedList
import io.wax911.sample.data.model.movie.Movie
import io.wax911.sample.data.repository.movie.MovieRepository
import io.wax911.support.core.viewmodel.SupportViewModel
import org.koin.core.KoinComponent
import org.koin.core.inject

class MovieViewModel : SupportViewModel<PagedList<Movie>>(), KoinComponent {

    override val repository by inject<MovieRepository>()
}