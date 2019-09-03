package co.anitrend.arch.data.mapper

import co.anitrend.arch.data.mapper.contract.ISupportDataMapper

/**
 * Provides functionality for mapping objects from one type to another
 *
 * @since 1.1.0
 */
abstract class SupportDataMapper<S, D>: ISupportDataMapper<S, D> {

    protected val moduleTag: String = javaClass.simpleName
}