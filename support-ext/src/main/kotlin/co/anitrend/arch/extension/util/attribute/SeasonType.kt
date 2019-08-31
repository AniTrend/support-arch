package co.anitrend.arch.extension.util.attribute

import androidx.annotation.StringDef

@Retention(AnnotationRetention.SOURCE)
@StringDef(
    SeasonType.WINTER,
    SeasonType.SPRING,
    SeasonType.SUMMER,
    SeasonType.FALL
)
annotation class SeasonType {
    companion object {
        const val WINTER = "WINTER"
        const val SPRING = "SPRING"
        const val SUMMER = "SUMMER"
        const val FALL = "FALL"

        val ALL = arrayOf(
            WINTER,
            SPRING,
            SUMMER,
            FALL
        )
        val SeasonsOfTheYear = arrayOf(
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
