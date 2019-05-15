package io.wax911.sample.core.viewmodel.show

import io.wax911.sample.core.model.show.Show
import io.wax911.support.core.repository.SupportRepository
import io.wax911.support.core.viewmodel.SupportViewModel
import org.koin.core.KoinComponent
import org.koin.core.inject

class ShowViewModel : SupportViewModel<List<Show>>(), KoinComponent {

    override val repository: SupportRepository<List<Show>> by inject()

}