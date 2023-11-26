package vlu.mobileproject.activity.view.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import vlu.mobileproject.R;
import vlu.mobileproject.activity.view.profile.Adapter.AccProfileSettingMenuAdapter;
import vlu.mobileproject.activity.view.profile.Model.AccProfileSettingMenuModel;
import vlu.mobileproject.globalfuction.ImageHandler;
import vlu.mobileproject.login.UserManager;
import vlu.mobileproject.modle.User;


public class Information_Account extends Information_Account_Detail {

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    //Switch switchMode;
    boolean nightMode;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    CircleImageView imgAvatarAccount;
    TextView NameAccount;
    ImageButton BtnGoToIn4Detail, btnGoToLanguage, btnGoToNotification, BtnBack;
    private String userID = UserManager.getInstance().getUserEmail();


    ArrayList<AccProfileSettingMenuModel> ProfileSettingList;
    AccProfileSettingMenuAdapter accProfileSettingMenuAdapter;
    RecyclerView ProfileSettingRec;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_account);
        addControls();
        addEvents();
        fetchUserDataFromFirebase(userID);

        ImageHandler.setImageFromFirebaseStorage(imgAvatarAccount, emailAccountLogin);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("MODE", Context.MODE_PRIVATE);

        // Get the current mode from SharedPreferences (default is light mode)
        nightMode = sharedPreferences.getBoolean("night", false);

        // Set the initial state of the Switch based on the current mode
        //switchMode.setChecked(nightMode);

        // Apply the mode to the entire app
        applyNightMode(nightMode);
        // we used sharedPreferences to save mode if exit the app and go back again
        sharedPreferences = getSharedPreferences("MODE", Context.MODE_PRIVATE);
        nightMode = sharedPreferences.getBoolean("night", false); //light mode is the default mode

/*        if (nightMode) {
            switchMode.setChecked(true);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        }*/

        ProfileSettingList = new ArrayList<>();
        ProfileSettingList.add(new AccProfileSettingMenuModel((R.drawable.language), "Ngôn ngữ", "language"));
        ProfileSettingList.add(new AccProfileSettingMenuModel((R.drawable.notification), "Thông báo", "notification"));
        ProfileSettingList.add(new AccProfileSettingMenuModel((R.drawable.darkmode), "Chế độ tối", "darkmode"));
        ProfileSettingList.add(new AccProfileSettingMenuModel((R.drawable.information), "Thông tin", "information"));
        ProfileSettingList.add(new AccProfileSettingMenuModel((R.drawable.baseline_logout_24), "Đăng xuất", "logout"));

        accProfileSettingMenuAdapter = new AccProfileSettingMenuAdapter(this, ProfileSettingList);

        ProfileSettingRec = findViewById(R.id.acc_hor_rec);
        ProfileSettingRec.setAdapter(accProfileSettingMenuAdapter);
        ProfileSettingRec.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        ProfileSettingRec.setHasFixedSize(true);
        ProfileSettingRec.setNestedScrollingEnabled(false);
    }

    @SuppressLint("CutPasteId")
    private void addControls() {
/*        switchMode = findViewById(R.id.switchDarkMode);
        ImageButton[] imageButtons = new ImageButton[]{
                findViewById(R.id.BtnGoToIn4Detail),
                findViewById(R.id.btnGoToLanguage),
                findViewById(R.id.btnGoToNotification),
        };
        BtnGoToIn4Detail = findViewById(R.id.BtnGoToIn4Detail);
        btnGoToLanguage = findViewById(R.id.btnGoToLanguage);
        btnGoToNotification = findViewById(R.id.btnGoToNotification);*/
        BtnBack = findViewById(R.id.BtnBack);
        imgAvatarAccount = findViewById(R.id.imgAvatarAccount);
        NameAccount = findViewById(R.id.NameAccount);
    }

    private void addEvents() {
/*        switchMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Save the selected mode to SharedPreferences when the user toggles the Switch
            editor = sharedPreferences.edit();
            editor.putBoolean("night", isChecked);
            editor.apply();

            // Apply the selected mode to the entire app
            applyNightMode(isChecked);
        });*/

        // change to activity_information_account_detail

/*        BtnGoToIn4Detail.setOnClickListener(v -> {
            // Start Information_Account_Detail activity
            Intent intent = new Intent(Information_Account.this, Information_Account_Detail.class);
            startActivity(intent);
        });*/


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

        // You can also apply the mode to other activities here (if needed) using the same method.
    }
    private void fetchUserDataFromFirebase(String userEmail) {
        FirebaseDatabase database = FirebaseDatabase.getInstance(bet);
        DatabaseReference myRef = database.getReference("User");

        // Query for the specific user with the target email
        Query query = myRef.orderByChild("user_email").equalTo(userEmail);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.hasChildren()) {
                    // Assuming there is only one user with the given email, retrieve the first child
                    DataSnapshot userSnapshot = snapshot.getChildren().iterator().next();

                    // Get the user object from the snapshot
                    User user = userSnapshot.getValue(User.class);

                    if (snapshot.exists()) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            if (user != null) {
                                String nameAcc = dataSnapshot.child("user_name").getValue(String.class);
                                NameAccount.setText(nameAcc);
                                ImageHandler.setImageFromFirebaseStorage(imgAvatarAccount, userEmail);
                            }
                        }
                    }

                } else {
                    // Handle the case when no user with the given email is found
                    Log.d("FirebaseError", "No user found with the email: " + userEmail);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error if needed
                Log.e("FirebaseError", "Error loading data from Firebase: " + error.getMessage());
            }
        });
    }
}