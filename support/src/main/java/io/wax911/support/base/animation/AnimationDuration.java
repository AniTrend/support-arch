package io.wax911.support.base.animation;

import androidx.annotation.IntDef;
import kotlin.annotation.AnnotationRetention;
import kotlin.annotation.Retention;

@Retention(AnnotationRetention.SOURCE)
@IntDef({AnimationDuration.SHORT, AnimationDuration.MEDIUM, AnimationDuration.LONG})
public @interface AnimationDuration {
    int SHORT = 250;
    int MEDIUM = 500;
    int LONG = 750;
}
