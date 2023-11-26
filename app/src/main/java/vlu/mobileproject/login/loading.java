package vlu.mobileproject.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import vlu.mobileproject.R;
import androidx.appcompat.app.AppCompatActivity;

public class loading extends AppCompatActivity {


    private static final int LOADING_DELAY = 2000; // 2 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(loading.this, StartupActivity.class);
                startActivity(intent);
                finish(); // Finish the loading activity to prevent going back to it on back press
            }
        }, LOADING_DELAY);
    }
}
