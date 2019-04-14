package io.wax911.support.extension

import com.annimon.stream.IntPair
import com.annimon.stream.Optional
import com.annimon.stream.Stream


/**
 * Gets the index of any type of iterable guaranteed that an equal override for the class
 * of type T is implemented.
 *
 * @param targetItem the item to search
 * @return 0 if no result was found
 */
fun <A> Iterable<A>?.indexOfIterable(targetItem: A?): Int = when (this != null) {
    targetItem != null -> {
        val pairOptional = Stream.of(this)
            .findIndexed { _, value -> value.equal(targetItem) }
        when {
            pairOptional.isPresent -> pairOptional.get().first
            else -> 0
        }
    }
    else -> 0
}

/**
 * Gets the index of any type of collection guaranteed that an
 * equal override for the class of type T is implemented.
 * <br/>
 * @see Object#equal(Object)
 *
 * @param targetItem the item to search
 * @return Optional result object
 * <br/>
 *
 * @see Optional<T> for information on how to handle return
 * @see IntPair
 */
fun <E> Collection<E>?.indexOfIntPair(targetItem: E?): Optional<IntPair<E>> = when {
    !isNullOrEmpty() -> Stream.of(this).findIndexed { _, value ->
        value.equal(targetItem)
    }
    else -> Optional.empty()
}

/**
 * Capitalizes all the strings unless they are specified in the exceptions list
 *
 * @param exceptions words or characters to exclude during capitalization
 * @return list of capitalized strings
 */
fun  Array<String>.capitalizeWords(exceptions: List<String>? = null) = Stream.of(*this)
    .map { s -> s.capitalizeWords(exceptions) }
    .toList()

/**
 * Gives the size of the collection by doing a null check first
 *
 * @return 0 if the collection is null or empty otherwise the size of the collection
 */
fun Collection<*>?.sizeOf() : Int = when {
    this.isNullOrEmpty() -> 0
    else -> size
}

/**
 * Clears the current list and adds the new items into the collection replacing all items
 */
fun <C> MutableList<C>.replaceWith(collection :Collection<C>) {
    clear(); addAll(collection)
}