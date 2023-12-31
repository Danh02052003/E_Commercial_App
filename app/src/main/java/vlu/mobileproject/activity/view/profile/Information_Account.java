package vlu.mobileproject.activity.view.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import vlu.mobileproject.ChooseLanguage;
import vlu.mobileproject.R;
import vlu.mobileproject.globalfuction.ImageHandler;
import vlu.mobileproject.login.UserManager;
import vlu.mobileproject.login.user;





public class Information_Account extends Information_Account_Detail {

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch switchMode;
    boolean nightMode;
    SharedPreferences sharedPreferences;
    //SharedPreferences.Editor editor;
    CircleImageView imgAvatarAccount;
    TextView NameAccount;
    ImageButton BtnGoToIn4Detail, btnGoToLanguage, btnGoToNotification, BtnBack;
    ConstraintLayout lLanguage, lNotify, lDarkmode, lIn4Detail;
    ImageView btnGoToIn4Detail, btnGotoIn4DetailEn;
    private final String userEmail = UserManager.getInstance().getUserEmail();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_account);
        addControls();
        addEvents();
        setPresentMode();
        fetchUserDataFromFirebase(userEmail);
    }

    private void setPresentMode(){
        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("MODE", Context.MODE_PRIVATE);

        // Get the current mode from SharedPreferences (default is light mode)
        nightMode = sharedPreferences.getBoolean("night", false);

        // Set the initial state of the Switch based on the current mode
        switchMode.setChecked(nightMode);

        // Apply the mode to the entire app
        applyNightMode(nightMode);
        // we used sharedPreferences to save mode if exit the app and go back again
        sharedPreferences = getSharedPreferences("MODE", Context.MODE_PRIVATE);
        nightMode = sharedPreferences.getBoolean("night", false); //light mode is the default mode

        if (nightMode) {
            switchMode.setChecked(true);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }

    }
    @SuppressLint("CutPasteId")
    private void addControls() {
        switchMode = findViewById(R.id.switchDarkMode);
        BtnBack = findViewById(R.id.BtnBack);
        imgAvatarAccount = findViewById(R.id.imgAvatarAccount);
        NameAccount = findViewById(R.id.NameAccount);
        BtnGoToIn4Detail= findViewById(R.id.BtnGoToIn4Detail);
        btnGoToLanguage = findViewById(R.id.btnGoToLanguage);
        btnGoToNotification = findViewById(R.id.btnGoToNotification);
        lLanguage = findViewById(R.id.lLanguage);
        lNotify = findViewById(R.id.lNotify);
        lDarkmode = findViewById(R.id.lDarkmode);
        lIn4Detail = findViewById(R.id.lIn4Detail);


        ImageHandler.setImageFromFirebaseStorage(imgAvatarAccount, userID);
    }

    private void addEvents() {


        lLanguage.setOnClickListener(v-> startNewActivity(ChooseLanguage.class));
        lIn4Detail.setOnClickListener(v -> startNewActivity(Information_Account_Detail.class));
        BtnBack.setOnClickListener(v -> finish());


        lDarkmode.setOnClickListener(v-> switchMode.setChecked(!switchMode.isChecked()));


        // Set OnCheckedChangeListener for the Switch
        switchMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Save the selected mode to SharedPreferences when the user toggles the Switch
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("night", isChecked);
            editor.apply();
            BtnGoToIn4Detail.setImageResource(R.drawable.gotowhite);
            btnGoToLanguage.setImageResource(R.drawable.gotowhite);
            btnGoToNotification.setImageResource(R.drawable.gotowhite);
            // Apply the selected mode to the entire app
            applyNightMode(isChecked);
        });


    }

    private void startNewActivity(Class<?> activityClass) {
        Intent intent = new Intent(Information_Account.this, activityClass);
        startActivity(intent);
    }

    protected void applyNightMode(boolean isNightMode) {
        // Apply the selected mode to the entire app
        if (isNightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
    private void fetchUserDataFromFirebase(String userEmail) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("User");
        FirebaseAuth auth = FirebaseAuth.getInstance();

        Query query = myRef.child(Objects.requireNonNull(auth.getCurrentUser()).getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean snapshotExists = snapshot.exists() && snapshot.hasChildren();
                if (snapshotExists) {
                    user CurUser = snapshot.getValue(user.class);
                    assert CurUser != null;
                    NameAccount.setText(CurUser.getUserName());
                    ImageHandler.setImageFromFirebaseStorage(imgAvatarAccount, auth.getCurrentUser().getUid());

                } else {
                    Log.d("FirebaseError", "No user found with the email: " + userEmail);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Error loading data from Firebase: " + error.getMessage());
            }
        });
    }
}