package io.wax911.support.base.attribute

import androidx.annotation.IntDef

@Retention(AnnotationRetention.SOURCE)
@IntDef(TimeTargetType.TIME_UNIT_DAYS, TimeTargetType.TIME_UNIT_HOURS,
        TimeTargetType.TIME_UNIT_MINUTES, TimeTargetType.TIME_UNITS_SECONDS)
annotation class TimeTargetType {
    companion object {

        const val TIME_UNIT_DAYS = 0
        const val TIME_UNIT_HOURS = 1
        const val TIME_UNIT_MINUTES = 2
        const val TIME_UNITS_SECONDS = 3
    }
}
