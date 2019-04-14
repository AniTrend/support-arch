package io.wax911.support.extension


/**
 * Potentially useless but returns an empty string, the signature may change in future
 *
 * @see String.isNullOrBlank
 */
fun String.Companion.empty() = ""


/**
 * Returns a copy of this strings having its first letter uppercase, or the original string,
 * if it's empty or already starts with an upper case letter.
 *
 * @param exceptions words or characters to exclude during capitalization
 */
fun String?.capitalizeWords(exceptions: List<String>? = null) : String = when {
    !this.isNullOrEmpty() -> {
        val result = StringBuilder(length)
        val words = split("_|\\s".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        for ((index, word) in words.withIndex()) {
            when (word.isNotEmpty()) {
                true -> {
                    if (!exceptions.isNullOrEmpty() && exceptions.contains(word)) result.append(word)
                    else result.append(word.capitalize())
                }
            }
            if (index != words.size - 1)
                result.append(" ")
        }
        result.toString()
    }
    else -> String.empty()
}

/**
 * Does an equality check as well as a nullability check
 *
 * @return true if two objects are the same otherwise
 *         false if one of them is null or both are not equal
 */
fun Any?.equal(b : Any?) : Boolean =
        this != null && b != null && this == b


