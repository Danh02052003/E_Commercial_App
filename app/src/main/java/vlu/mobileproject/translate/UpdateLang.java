package vlu.mobileproject.translate;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.TextView;

public class UpdateLang {
    public static void translateText(TextView[] textViews, Context context) {
        String inputText;
        String language = getLanguage(context);

        for (TextView tv : textViews) {
            inputText = tv.getText().toString();
            new TranslationTask(tv, language).execute(inputText);
        }
    }

    public static void exchangeCurrency(TextView[] textViews, Context context) {
        String inputText;
        String language = getLanguage(context);

        for (TextView tv : textViews) {
            inputText = tv.getText().toString();
            // Adjust parameters based on your needs
            new ExchangeTask(tv, language.equals("en") ? "VND" : "USD",
                    language.equals("en") ? "USD" : "VND").execute(inputText);
        }
    }

    public static String getLanguage(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        return prefs.getString("language", "vi");
    }

    public static void setLanguage(String lang, Context context) {
        SharedPreferences prefs = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        prefs.edit().putString("language", lang).apply();
    }
}
