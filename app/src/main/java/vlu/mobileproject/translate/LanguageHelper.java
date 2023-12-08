package vlu.mobileproject.translate;


import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.Log;

import java.util.Locale;

import vlu.mobileproject.ChooseLanguage;

public class LanguageHelper {
    public static void changeLanguage(Resources resources, String languageCode) {
        Log.d("LanguageChange", "Switching to language: " + languageCode);

        Locale newLocale = new Locale(languageCode);
        Locale.setDefault(newLocale);

        Configuration configuration = new Configuration(resources.getConfiguration());
        configuration.setLocale(newLocale);

        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }
}
