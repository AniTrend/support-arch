package io.wax911.support.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ShortcutManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;

@TargetApi(Build.VERSION_CODES.N_MR1)
@RequiresApi(Build.VERSION_CODES.N_MR1)
public abstract class ShortcutUtil {

    private static <S> Intent createIntentAction(Context context, Class<S> targetActivity, Bundle param) {
        Intent intent = new Intent(context, targetActivity);
        intent.putExtras(param);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return intent;
    }

    public static ShortcutManager getShortcutManager(Context context) {
        return context.getSystemService(ShortcutManager.class);
    }

    public static void removeAllDynamicShortcuts(Context context) {
        getShortcutManager(context).removeAllDynamicShortcuts();
    }
}
