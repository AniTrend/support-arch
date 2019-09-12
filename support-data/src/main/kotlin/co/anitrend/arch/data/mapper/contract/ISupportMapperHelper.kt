package co.anitrend.arch.data.mapper.contract

/**
 * Mapper contract for transform one type to the other
 *
 * @since v1.2.0
 */
interface ISupportMapperHelper<in S, out D> {

    /**
     * Transforms the the [source] to the target type
     */
    fun transform(source: S): D
}