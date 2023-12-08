package vlu.mobileproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import vlu.mobileproject.translate.LanguageHelper;
import vlu.mobileproject.translate.UpdateLang;

public class ChooseLanguage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_language);
        LinearLayout lVi = findViewById(R.id.lVi);
        LinearLayout lEn = findViewById(R.id.lEn);
        ImageButton BtnBack = findViewById(R.id.BtnBack);


        BtnBack.setOnClickListener(v-> finish());

        lVi.setOnClickListener(v -> {
            if ("en".equals(UpdateLang.getLanguage(this))) {
                UpdateLang.setLanguage("vi", this);
                Log.d("LanguageChange", "Switching to Vietnamese");
                LanguageHelper.changeLanguage(getResources(), "vi");
                recreate();
                finish();
            }else Toast.makeText(this, getString(R.string.error_language), Toast.LENGTH_LONG).show();
        });

        lEn.setOnClickListener(v -> {
            if ("vi".equals(UpdateLang.getLanguage(this))) {
                UpdateLang.setLanguage("en", this);
                Log.d("LanguageChange", "Switching to English");
                LanguageHelper.changeLanguage(getResources(), "en");
                recreate();
                finish();
            }else Toast.makeText(this, getString(R.string.error_language), Toast.LENGTH_LONG).show();
        });
    }

    private boolean getNightModeStatus() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("night", false);
    }
}
