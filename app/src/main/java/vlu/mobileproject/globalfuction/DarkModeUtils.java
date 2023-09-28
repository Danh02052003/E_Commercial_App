package vlu.mobileproject.globalfuction;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;

public class DarkModeUtils {

    private static final String SHARED_PREFS_NAME = "MODE";
    private static final String KEY_NIGHT_MODE = "night";

    public static void applyNightMode(Context context, boolean isNightMode) {
        AppCompatDelegate.setDefaultNightMode(isNightMode
                ? AppCompatDelegate.MODE_NIGHT_YES
                : AppCompatDelegate.MODE_NIGHT_NO);

        // Save the selected mode to SharedPreferences
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_NIGHT_MODE, isNightMode);
        editor.apply();
    }

    public static boolean isNightModeEnabled(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(KEY_NIGHT_MODE, false);
    }
}
