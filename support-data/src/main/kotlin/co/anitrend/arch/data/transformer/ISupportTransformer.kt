package co.anitrend.arch.data.transformer

/**
 * Mapper contract for transform one type to the other
 *
 * @since v1.2.0
 */
interface ISupportTransformer<in S, out D> {

    /**
     * Transforms the the [source] to the target type
     */
    fun transform(source: S): D
}