package io.wax911.sample.core.viewmodel.show

import android.os.Bundle
import androidx.paging.PagedList
import io.wax911.sample.data.model.show.Show
import io.wax911.sample.data.repository.show.ShowRepository
import io.wax911.support.core.viewmodel.SupportViewModel
import org.koin.core.KoinComponent
import org.koin.core.inject

class ShowViewModel(
    showRepository: ShowRepository
) : SupportViewModel<PagedList<Show>, Bundle>(showRepository)