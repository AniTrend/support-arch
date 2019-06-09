package io.wax911.sample.core.viewmodel.show

import androidx.paging.PagedList
import io.wax911.sample.data.model.show.Show
import io.wax911.sample.data.repository.show.ShowRepository
import io.wax911.support.core.viewmodel.SupportViewModel
import org.koin.core.KoinComponent
import org.koin.core.inject

class ShowViewModel : SupportViewModel<PagedList<Show>>(), KoinComponent {

    override val repository by inject<ShowRepository>()
}