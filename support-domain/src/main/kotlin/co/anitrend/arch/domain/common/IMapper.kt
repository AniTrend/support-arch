package co.anitrend.arch.domain.common

/**
 *
 * @since 1.2.0
 */
interface IMapper<in S, D> {

    /**
     * Maps an object from one type to another
     *
     * @param source original object
     */
    fun mapFrom(source: S): D
}