package co.anitrend.arch.extension.util.attribute

enum class SeasonType(value: String) {
    WINTER("WINTER"),
    SPRING("SPRING"),
    SUMMER("SUMMER"),
    FALL("FALL");

    companion object {
        val seasonsOfTheYear = arrayOf(
            WINTER,
            WINTER,
            SPRING,
            SPRING,
            SPRING,
            SUMMER,
            SUMMER,
            SUMMER,
            FALL,
            FALL,
            FALL,
            WINTER
        )
    }
}
