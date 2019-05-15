package io.wax911.sample.core.viewmodel.movie

import androidx.paging.PagedList
import io.wax911.sample.core.model.movie.Movie
import io.wax911.sample.core.util.StateUtil
import io.wax911.support.core.repository.SupportRepository
import io.wax911.support.core.viewmodel.SupportViewModel
import org.koin.core.KoinComponent
import org.koin.core.inject

class MovieViewModel : SupportViewModel<List<Movie>>(), KoinComponent {

    override val config: PagedList.Config = PagedList.Config.Builder()
        .setMaxSize(StateUtil.pagingLimit * 2)
        .setPageSize(StateUtil.pagingLimit)
        .setEnablePlaceholders(false)
        .build()

    override val repository: SupportRepository<List<Movie>> by inject()

}