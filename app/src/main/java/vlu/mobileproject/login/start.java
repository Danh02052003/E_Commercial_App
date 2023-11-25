package vlu.mobileproject.login;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import vlu.mobileproject.R;
import vlu.mobileproject.translate.LanguageHelper;

public class start extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        LanguageHelper.changeLanguage(getResources(),"en");
    }


    public void onStartButtonClick(View view) {

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        recreate();
        finish();
    }
}

