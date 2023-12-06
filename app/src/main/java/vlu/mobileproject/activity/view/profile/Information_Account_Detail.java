package vlu.mobileproject.activity.view.profile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import vlu.mobileproject.R;
import vlu.mobileproject.globalfuction.ImageHandler;
import vlu.mobileproject.modle.User;


public class Information_Account_Detail extends AppCompatActivity {
    ImageButton BtnBack, btnChecked;
    EditText edtNameAccount, edtEmailAccount, edtSDTAccount, edtAddressAccount;
    TextView uploadAvatar;
    static CircleImageView imgAvatarAccount;
    static String bet = "https://e-commerce-73482-default-rtdb.asia-southeast1.firebasedatabase.app/";
    // email or sdt from login account

    private final String userEmail = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    String emailAccountLogin = userEmail;

    // use for avatar
    String shortEmailAccountLogin = emailAccountLogin.replace("@gmail.com", "");

    FirebaseDatabase firebaseDatabase;
    DatabaseReference UserReference;

    // Firebase Storage reference
    private StorageReference storageRef;
    public String avatarUrl = "https://firebasestorage.googleapis.com/v0/b/e-commerce-73482.appspot.com/o/avatars%2Favatar_" + shortEmailAccountLogin +".png?alt=media";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_account_detail);

        firebaseDatabase = FirebaseDatabase.getInstance();
        UserReference = firebaseDatabase.getReference("User");

        addControls();
        addEvents();
        storageRef = FirebaseStorage.getInstance().getReference();
        SetupUserData2View();

    }

    private void addEvents() {
        // Handle the click event for the Back button
        BtnBack.setOnClickListener(v -> finish());

        // Handle the click event for the Checked button
        btnChecked.setOnClickListener(v -> {
            saveAvatarAndNavigateBack();
            updateInformationDetail(emailAccountLogin);
            Toast.makeText(Information_Account_Detail.this, "Save successfully", Toast.LENGTH_SHORT).show();
        });

        // upload avatar
        uploadAvatar.setOnClickListener(v -> {
            // Create an intent to open the user's library album
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 1);
        });
    }

    // update data user
    private void updateInformationDetail(String userEmail) {
        FirebaseDatabase database = FirebaseDatabase.getInstance(bet);
        DatabaseReference myRef = database.getReference("User");

        // Query for the specific user with the target email
        Query query = myRef.orderByChild("user_email").equalTo(userEmail);

        String newNameAccount = edtNameAccount.getText().toString();
        String newEmailAccount = edtEmailAccount.getText().toString();
        String newSdtAccount = edtSDTAccount.getText().toString();
        if (newSdtAccount.length() != 12) {
            Toast.makeText(this, "\n" +
                    "Số điện thoại phải có chính xác 10 chữ số", Toast.LENGTH_SHORT).show();
        }
        else if (!newNameAccount.equals("") && !newEmailAccount.equals("") && !newSdtAccount.equals("")) {
            // Create a HashMap to hold the updated values
            HashMap<String, Object> updatedValues = new HashMap<>();
            updatedValues.put("user_name", newNameAccount);
            updatedValues.put("user_email", newEmailAccount);
            updatedValues.put("user_phone", newSdtAccount);

            // Update the user information in the database
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists() && snapshot.hasChildren()) {
                        // Assuming there is only one user with the given email, retrieve the first child
                        DataSnapshot userSnapshot = snapshot.getChildren().iterator().next();

                        // Get the user ID from the snapshot
                        String userId = userSnapshot.getKey();

                        // Update the user information with the new values
                        assert userId != null;
                        myRef.child(userId).updateChildren(updatedValues)
                                .addOnSuccessListener(aVoid -> {
                                    // Update successful
                                    Log.d("FirebaseDebug", "User information updated successfully.");
                                })
                                .addOnFailureListener(e -> {
                                    // Update failed
                                    Log.e("FirebaseError", "Failed to update user information: " + e.getMessage());
                                });
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
        } else {
            // Handle the case when any of the new information fields are empty
            Log.d("FirebaseDebug", "New information fields are empty. No update performed.");
        }
    }

    void SetupUserData2View() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String UserID = auth.getCurrentUser().getUid();
        Query query = UserReference.child(UserID);
        String userEmail = auth.getCurrentUser().getEmail();
        String userPhoneNumb = auth.getCurrentUser().getPhoneNumber();
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    edtNameAccount.setText(snapshot.child("userName").getValue(String.class));
                    edtEmailAccount.setText(userEmail);
                    edtSDTAccount.setText(userPhoneNumb);
                    ImageHandler.setImageFromFirebaseStorage(imgAvatarAccount, userEmail);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            // Get the image URI from the selected image
            Uri imageUri = data.getData();

            // Set the selected image to the 'imgAvatarAccount' CircleImageView
            imgAvatarAccount.setImageURI(imageUri);
        }
    }

    private void addControls() {
        // Initialize views
        BtnBack = findViewById(R.id.BtnBack);
        btnChecked = findViewById(R.id.btnChecked);
        edtNameAccount = findViewById(R.id.edtNameAccount);
        edtEmailAccount = findViewById(R.id.edtEmailAccount);
        edtSDTAccount = findViewById(R.id.edtSDTAccount);
        edtAddressAccount = findViewById(R.id.edtAddressAccount);
        uploadAvatar = findViewById(R.id.uploadAvatar);
        imgAvatarAccount = findViewById(R.id.imgAvatarAccount);
    }

    private void saveAvatarAndNavigateBack() {
        // Get the selected image URI from the 'imgAvatarAccount' CircleImageView
        Uri imageUri = getImageUri();

        if (imageUri != null) {
            // Upload the image to Firebase Storage and get the download URL
            uploadImageToFirebase(imageUri, new UploadImageCallback() {
                @Override
                public void onSuccess(Uri downloadUri) {
                    // Save the download URL in Firebase Realtime Database under 'avatar_url' node
                    // (already handled in the uploadImageToFirebase method)

                    // Navigate back to the previous screen
                    finish();
                }

                @Override
                public void onSuccess(String downloadUrl) {

                }

                @Override
                public void onFailure(String errorMessage) {
                    // Handle image upload failure, if needed
                }
            });
        } else {
            // If no image is selected, simply navigate back to the previous screen
            finish();
        }
    }

    private Uri getImageUri() {
        // Get the image URI from the 'imgAvatarAccount' CircleImageView
        Drawable drawable = imgAvatarAccount.getDrawable();
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            bitmap = ((BitmapDrawable) drawable).getBitmap();
        }

        if (bitmap != null) {
            // Save the image to a temporary file and return its URI
            try {
                File tempFile = File.createTempFile("avatar", ".png");
                FileOutputStream out = new FileOutputStream(tempFile);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.flush();
                out.close();
                return Uri.fromFile(tempFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    private void uploadImageToFirebase(Uri imageUri, UploadImageCallback callback) {
        // Get the file name for the image
        String fileName = "avatar_" + shortEmailAccountLogin + ".png";

        // Get the reference to the location in Firebase Storage where you want to save the image
        StorageReference avatarRef = storageRef.child("avatars").child(fileName);

        // Upload the image to Firebase Storage
        UploadTask uploadTask = avatarRef.putFile(imageUri);

        // Add an OnSuccessListener to get the download URL after the upload is successful
        uploadTask.addOnSuccessListener(taskSnapshot -> {
            // Get the download URL of the uploaded image
            avatarRef.getDownloadUrl().addOnSuccessListener(downloadUri -> {
                // Callback with the download URL as a string
                String downloadUrlString = downloadUri.toString();
                callback.onSuccess(downloadUrlString);

                // Save the avatar URL in the database after successfully uploading the image
                saveAvatarUrlToDatabase(downloadUrlString);
            }).addOnFailureListener(e -> {
                // Handle failure to get the download URL, if needed
                callback.onFailure("Failed to get the download URL.");
            });
        }).addOnFailureListener(e -> {
            // Handle failure during image upload, if needed
            callback.onFailure("Failed to upload image.");
        });
    }

    private void saveAvatarUrlToDatabase(String avatarUrl) {
        // Save the avatar URL in Firebase Realtime Database under the 'avatar_url' node for the current user
        FirebaseDatabase database = FirebaseDatabase.getInstance(bet);
        DatabaseReference myRef = database.getReference("User");

        String currentUserUid = "Replace_with_current_user_uid"; // Replace this with the actual user ID
        DatabaseReference currentUserRef = myRef.child(currentUserUid);

        currentUserRef.child("avatar_url").setValue(avatarUrl).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d("FirebaseDebug", "Avatar URL saved successfully.");
            } else {
                Log.e("FirebaseError", "Failed to save Avatar URL: " + Objects.requireNonNull(task.getException()).getMessage());
            }
        });
    }


    private interface UploadImageCallback {
        void onSuccess(Uri downloadUri);

        void onSuccess(String downloadUrl);

        void onFailure(String errorMessage);
    }
}