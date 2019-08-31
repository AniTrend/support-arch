package co.anitrend.arch.extension


/**
 * Capitalizes all the strings unless they are specified in the exceptions list
 *
 * @param exceptions words or characters to exclude during capitalization
 * @return list of capitalized strings
 */
fun  Collection<String>.capitalizeWords(exceptions: List<String>? = null) =
    map { it.capitalizeWords(exceptions) }


/**
 * Gives the size of the collection by doing a null check first
 *
 * @return 0 if the collection is null or empty otherwise the size of the collection
 */
fun Collection<*>?.sizeOf() = when {
    this.isNullOrEmpty() -> 0
    else -> size
}

/**
 * Clears the current list and adds the new items into the collection replacing all items
 */
fun <C> MutableList<C>.replaceWith(collection :Collection<C>) {
    clear(); addAll(collection)
}