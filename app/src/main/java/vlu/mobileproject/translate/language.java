package vlu.mobileproject.translate;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class language extends Application {

    private static language instance;
    private static SharedPreferences preferences;
    private static final String KEY_PRESENT_LANG = "presentLang";

    private static String presentLang;

    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize SharedPreferences in onCreate instead of the constructor
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        // Retrieve the saved language preference
        presentLang = preferences.getString(KEY_PRESENT_LANG, "vi");
    }

    public static synchronized language getInstance() {
        if (instance == null) {
            instance = new language();
        }
        return instance;
    }

    public static String getPresentLang() {
        return presentLang;
    }

    public static void setPresentLang(String lang) {
        // Save the selected language preference
        preferences.edit().putString(KEY_PRESENT_LANG, lang).apply();
        presentLang = lang;
    }
}

