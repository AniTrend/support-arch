package io.wax911.sample.domain.usecases.show

import android.os.Parcelable
import io.wax911.sample.domain.common.IPagedMediaUseCase
import io.wax911.sample.domain.repositories.show.ITraktShowRepository
import co.anitrend.arch.domain.common.IUserInterfaceState
import kotlinx.android.parcel.Parcelize

abstract class TraktShowUseCase<R: IUserInterfaceState<*>>(
    private val showRepository: ITraktShowRepository<R>
) : IPagedMediaUseCase<TraktShowUseCase.Payload, R> {

    /**
     * Solves a given use case in the implementation target
     *
     * @param param input for solving a given use case
     */
    override fun invoke(param: Payload): R {
        return when (param.requestType) {
            ShowRequestType.ShowPopular -> showRepository.getPopularShows()
            ShowRequestType.ShowTrending -> showRepository.getTrendingShows()
        }
    }

    @Parcelize
    data class Payload(
        val requestType: ShowRequestType
    ) : Parcelable
}