package io.wax911.support.base.attribute;

import androidx.annotation.StringDef;
import kotlin.annotation.AnnotationRetention;
import kotlin.annotation.Retention;

@Retention(AnnotationRetention.SOURCE)
@StringDef({SeasonType.WINTER, SeasonType.SPRING,
        SeasonType.SUMMER, SeasonType.FALL})
public @interface SeasonType {
    String WINTER = "WINTER",
            SPRING = "SPRING",
            SUMMER = "SUMMER",
            FALL = "FALL";

    String[] Seasons = {WINTER, SPRING, SUMMER, FALL};
}
