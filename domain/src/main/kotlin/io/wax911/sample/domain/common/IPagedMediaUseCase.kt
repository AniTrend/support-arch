package io.wax911.sample.domain.common

import co.anitrend.arch.domain.common.IUserInterfaceState
import co.anitrend.arch.domain.usecases.core.ISupportCoreUseCase

/**
 * Contract for paging use cases
 */
interface IPagedMediaUseCase<P, R : IUserInterfaceState<*>>: ISupportCoreUseCase<P, R>