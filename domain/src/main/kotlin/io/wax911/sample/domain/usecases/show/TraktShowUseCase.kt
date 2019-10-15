package io.wax911.sample.domain.usecases.show

import android.os.Parcelable
import co.anitrend.arch.domain.common.IUserInterfaceState
import io.wax911.sample.domain.common.IPagedMediaUseCase
import io.wax911.sample.domain.repositories.show.ITraktShowRepository
import kotlinx.android.parcel.Parcelize

abstract class TraktShowUseCase<R: IUserInterfaceState<*>>(
    protected val repository: ITraktShowRepository<R>
) : IPagedMediaUseCase<TraktShowUseCase.Payload, R> {

    /**
     * Solves a given use case in the implementation target
     *
     * @param param input for solving a given use case
     */
    override fun invoke(param: Payload): R {
        return when (param.requestType) {
            ShowRequestType.ShowPopular -> repository.getPopularShows()
            ShowRequestType.ShowTrending -> repository.getTrendingShows()
        }
    }

    @Parcelize
    data class Payload(
        val requestType: ShowRequestType
    ) : Parcelable
}