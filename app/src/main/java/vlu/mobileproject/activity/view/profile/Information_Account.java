package vlu.mobileproject.activity.view.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;
import vlu.mobileproject.R;
import vlu.mobileproject.globalfuction.ImageHandler;
import vlu.mobileproject.login.UserManager;
import vlu.mobileproject.login.user;
import vlu.mobileproject.modle.User;


public class Information_Account extends Information_Account_Detail {

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch switchMode;
    boolean nightMode;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    CircleImageView imgAvatarAccount;
    TextView NameAccount;
    ImageButton BtnGoToIn4Detail, btnGoToLanguage, btnGoToNotification, BtnBack;
    private String userEmail = UserManager.getInstance().getUserEmail();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_account);
        addControls();
        addEvents();
        fetchUserDataFromFirebase(userEmail);

        ImageHandler.setImageFromFirebaseStorage(imgAvatarAccount, emailAccountLogin);

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
        ImageButton[] imageButtons = new ImageButton[]{
                findViewById(R.id.BtnGoToIn4Detail),
                findViewById(R.id.btnGoToLanguage),
                findViewById(R.id.btnGoToNotification),
        };
        BtnGoToIn4Detail = findViewById(R.id.BtnGoToIn4Detail);
        btnGoToLanguage = findViewById(R.id.btnGoToLanguage);
        btnGoToNotification = findViewById(R.id.btnGoToNotification);
        BtnBack = findViewById(R.id.BtnBack);
        imgAvatarAccount = findViewById(R.id.imgAvatarAccount);
        NameAccount = findViewById(R.id.NameAccount);

    }

    private void addEvents() {
        switchMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Save the selected mode to SharedPreferences when the user toggles the Switch
            editor = sharedPreferences.edit();
            editor.putBoolean("night", isChecked);
            editor.apply();

            // Apply the selected mode to the entire app
            applyNightMode(isChecked);
        });

        // change to activity_information_account_detail

        BtnGoToIn4Detail.setOnClickListener(v -> {
            // Start Information_Account_Detail activity
            Intent intent = new Intent(Information_Account.this, Information_Account_Detail.class);
            startActivity(intent);
        });


        BtnBack.setOnClickListener(v -> {
            // Go back to the previous fragment or finish the activity if no fragments in the back stack
            onBackPressed();
        });
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

        Query query = myRef.child(auth.getCurrentUser().getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean snapshotExists = snapshot.exists() && snapshot.hasChildren();
                if (snapshotExists) {
                    user CurUser = snapshot.getValue(user.class);
                    NameAccount.setText(CurUser.getUserName());
                    ImageHandler.setImageFromFirebaseStorage(imgAvatarAccount, auth.getCurrentUser().getEmail());

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