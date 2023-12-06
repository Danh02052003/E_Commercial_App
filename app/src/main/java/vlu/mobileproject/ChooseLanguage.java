package vlu.mobileproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class ChooseLanguage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_language);

        // Get ListView reference
        ListView languageListView = findViewById(R.id.LVlanguage);

        // Create a list of LanguageItem objects
        ArrayList<LanguageItem> languageList = new ArrayList<>();
        languageList.add(new LanguageItem("Vietnamese", R.drawable.flag_vietnam, "vi")); // "vi" for Vietnamese
        languageList.add(new LanguageItem("English", R.drawable.flag_english, "en")); // "en" for English

        // Create custom adapter and set it to the ListView
        LanguageAdapter adapter = new LanguageAdapter(this, languageList);
        languageListView.setAdapter(adapter);

        // Set item click listener
        languageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, android.view.View view, int position, long id) {
                // Get the selected LanguageItem
                LanguageItem selectedLanguage = languageList.get(position);

                // Save the selected language to SharedPreferences
                saveLanguagePreference(selectedLanguage.languageCode());

                // Optionally, you can restart the activity or reload your UI to apply the language change
                recreate();
            }
        });
    }

    private void saveLanguagePreference(String languageCode) {
        SharedPreferences preferences = getSharedPreferences("LanguagePreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("language_code", languageCode);
        editor.apply();
    }
}
