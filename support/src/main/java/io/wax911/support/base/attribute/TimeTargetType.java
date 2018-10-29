package io.wax911.support.base.attribute;

import androidx.annotation.IntDef;
import kotlin.annotation.AnnotationRetention;
import kotlin.annotation.Retention;

@Retention(AnnotationRetention.SOURCE)
@IntDef({TimeTargetType.TIME_UNIT_DAYS, TimeTargetType.TIME_UNIT_HOURS,
        TimeTargetType.TIME_UNIT_MINUTES, TimeTargetType.TIME_UNITS_SECONDS})
public @interface TimeTargetType {

    int TIME_UNIT_DAYS = 0,
            TIME_UNIT_HOURS = 1,
            TIME_UNIT_MINUTES = 2,
            TIME_UNITS_SECONDS = 3;
}
