package co.anitrend.arch.data.mapper

import co.anitrend.arch.data.mapper.contract.ISupportResponseMapper

/**
 * Provides functionality for mapping objects from one type to another
 *
 * @since 1.1.0
 */
abstract class SupportResponseMapper<S, D> : ISupportResponseMapper<S, D> {

    /**
    * Current module identifier tag
    */
    protected val moduleTag: String = javaClass.simpleName
}