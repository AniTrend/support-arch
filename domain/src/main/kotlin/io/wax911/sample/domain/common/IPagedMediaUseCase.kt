package io.wax911.sample.domain.common

import co.anitrend.arch.domain.common.IUserInterfaceState
import co.anitrend.arch.domain.usecases.ISupportUseCase

/**
 * Contract for paging use cases
 */
interface IPagedMediaUseCase<P, R : IUserInterfaceState<*>>: ISupportUseCase<P, R>