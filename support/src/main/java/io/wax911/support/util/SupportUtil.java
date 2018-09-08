package io.wax911.support.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.annimon.stream.IntPair;
import com.annimon.stream.Objects;
import com.annimon.stream.Optional;
import com.annimon.stream.Stream;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import androidx.annotation.ArrayRes;
import androidx.annotation.AttrRes;
import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityManagerCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.util.Pair;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.FragmentActivity;
import io.wax911.support.R;
import okhttp3.Cache;

@SuppressWarnings({"unused", "WeakerAccess"})
public class SupportUtil {

    protected static final int CACHE_LIMIT = 1024 * 1024 * 250;

    /**
     * Avoids resource not found when using vector drawables in API levels < Lollipop
     *
     * @param resource The resource id of the drawable or vector drawable
     *                 @see DrawableRes
     *
     * @param context Any valid application context
     *                @see Context
     *
     * @return Drawable
     * @see Drawable
     */
    public static Drawable getDrawable(Context context, @DrawableRes int resource) {
        return AppCompatResources.getDrawable(context, resource);
    }

    /**
     * Avoids resource not found when using vector drawables in API levels < Lollipop
     * and tints the drawable depending on the current selected theme, images loaded
     * from this method apply the {@link Drawable#mutate()} to assure that the state
     * of each drawable is not shared
     *
     * @param resource The resource id of the drawable or vector drawable
     *                 @see DrawableRes
     *
     * @param context Any valid application context
     *                @see Context
     *
     * @return Drawable
     * @see Drawable
     */
    public static Drawable getTintedDrawable(Context context, @DrawableRes int resource) {
        Drawable drawable = DrawableCompat.wrap(Objects.requireNonNull(AppCompatResources.getDrawable(context, resource))).mutate();
        DrawableCompat.setTint(drawable, getColorFromAttr(context, R.attr.titleColor));
        return drawable;
    }

    /**
     * Avoids resource not found when using vector drawables in API levels < Lollipop
     * Also images loaded from this method apply the {@link Drawable#mutate()} to assure
     * that the state of each drawable is not shared
     *
     * @param resource The resource id of the drawable or vector drawable
     *                 @see DrawableRes
     *
     * @param context Any valid application context
     *                @see Context
     *
     * @param tint A specific color to tint the drawable
     *
     * @return Drawable
     * @see Drawable
     */
    public static Drawable getDrawable(Context context, @DrawableRes int resource, @ColorRes int tint) {
        Drawable drawable = DrawableCompat.wrap(Objects.requireNonNull(AppCompatResources.getDrawable(context, resource))).mutate();
        if(tint != 0)
            DrawableCompat.setTint(drawable, getColor(context, tint));
        return drawable;
    }

    /**
     * Avoids resource not found when using vector drawables in API levels < Lollipop
     * Also images loaded from this method apply the {@link Drawable#mutate()} to assure
     * that the state of each drawable is not shared
     *
     * @param resource The resource id of the drawable or vector drawable
     *                 @see DrawableRes
     *
     * @param context Any valid application context
     *                @see Context
     *
     * @param attribute Type of attribute resource
     *
     * @return Drawable
     * @see Drawable
     */
    public static Drawable getDrawableTintAttr(Context context, @DrawableRes int resource, @AttrRes int attribute) {
        Drawable drawable = DrawableCompat.wrap(Objects.requireNonNull(AppCompatResources.getDrawable(context, resource))).mutate();
        DrawableCompat.setTint(drawable, getColorFromAttr(context, attribute));
        return drawable;
    }

    /**
     * Returns a color from a defined attribute
     *
     * @param context Any valid application context
     *                @see Context
     *
     * @param attribute Type of attribute resource
     *
     * @return Color Integer
     */
    public static @ColorInt int getColorFromAttr(Context context, @AttrRes int attribute) {
        TypedArray colorAttribute = context.obtainStyledAttributes(new int[] {attribute});
        @ColorInt int color = colorAttribute.getColor(0, 0);
        colorAttribute.recycle();
        return color;
    }

    public static int dipToPx(float dpValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int pxToDip(float pxValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int spToPx(float spValue) {
        float scaledDensity = Resources.getSystem().getDisplayMetrics().scaledDensity;
        return Math.round(spValue * scaledDensity);
    }

    /**
     * Return true if the smallest width in DP of the device is equal or greater than the given
     * value.
     */
    public static boolean isScreenSw(int swDp) {
        DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();
        float widthDp = displayMetrics.widthPixels / displayMetrics.density;
        float heightDp = displayMetrics.heightPixels / displayMetrics.density;
        float screenSw = Math.min(widthDp, heightDp);
        return screenSw >= swDp;
    }

    /**
     * Return true if the width in DP of the device is equal or greater than the given value
     */
    public static boolean isScreenW(int widthDp) {
        DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();
        float screenWidth = displayMetrics.widthPixels / displayMetrics.density;
        return screenWidth >= widthDp;
    }

    public static int getColor(Context context, @ColorRes int color) {
        return ContextCompat.getColor(context, color);
    }
    
    /**
     * Sorts a given map by the order of the of the keys in the map in descending order
     * @see ComparatorUtil#getKeyComparator() for logic on how comparator handles this
     */
    public static <T> List<Map.Entry<String, T>> getKeyFilteredMap(Map<String, T> map) {
        return Stream.of(map).sorted(ComparatorUtil.getKeyComparator()).toList();
    }

    @Deprecated
    public static boolean isLowRamDevice(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        boolean isLowRamDevice = false;
        if (activityManager != null)
            isLowRamDevice = ActivityManagerCompat.isLowRamDevice(activityManager);
        return isLowRamDevice;
    }

    @Deprecated
    public static <T extends Collection> boolean isEmpty(@Nullable T collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean equals(Object a, Object b) {
        return (a != null && a.equals(b));
    }
}
