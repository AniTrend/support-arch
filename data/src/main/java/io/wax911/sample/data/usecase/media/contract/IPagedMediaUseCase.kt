package io.wax911.sample.data.usecase.media.contract

import android.os.Parcelable
import androidx.paging.PagedList
import io.wax911.sample.data.model.contract.TraktEntity
import io.wax911.sample.data.usecase.media.MediaRequestType
import io.wax911.support.data.model.UiModel
import io.wax911.support.data.usecase.core.ISupportCoreUseCase
import kotlinx.android.parcel.Parcelize

interface IPagedMediaUseCase<T : TraktEntity> :
    ISupportCoreUseCase<IPagedMediaUseCase.Payload, UiModel<PagedList<T>>> {

    @Parcelize
    data class Payload(
        @MediaRequestType
        val requestType: String
    ) : Parcelable
}