package vlu.mobileproject.translate;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.Locale;

public class LanguageHelper {

    public static void changeLanguage(Resources resources, String languageCode) {
        Locale newLocale = new Locale(languageCode);
        Locale.setDefault(newLocale);

        Configuration configuration = new Configuration(resources.getConfiguration());
        configuration.setLocale(newLocale);

        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }
}
