package vlu.mobileproject.translate;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.TextView;

import java.util.List;

public class UpdateLang {
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

    public static void translateLanguage(TextView[] textViews, Context context){
        String inputText;
        String language = getLanguage(context);

        for(TextView tv: textViews){
            inputText = tv.getText().toString();

            new TranslationTask(language.equals("en")? "en": "vi", tv).execute(inputText);
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

    public static void saveTextValues(List<TextView> textViews, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();

        for (int i = 0; i < textViews.size(); i++) {
            String key = "text_view_" + i;
            String value = textViews.get(i).getText().toString();
            editor.putString(key, value);
        }

        editor.apply();
    }

    public static void restoreTextValues(List<TextView> textViews, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        for (int i = 0; i < textViews.size(); i++) {
            String key = "text_view_" + i;
            String value = preferences.getString(key, "");
            textViews.get(i).setText(value);
        }
    }
}
