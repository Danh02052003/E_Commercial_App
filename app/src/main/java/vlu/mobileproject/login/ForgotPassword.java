package vlu.mobileproject.login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import vlu.mobileproject.R;
import vlu.mobileproject.login.LoginActivity;

public class ForgotPassword extends AppCompatActivity {
    private TextView textLogin,bttdangnhap;
    private EditText editTextEmail;
    private Button buttonResetPassword;
    private EditText edtNewPassword;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        textLogin = findViewById(R.id.dangNhap);
        editTextEmail = findViewById(R.id.edtEmail);
        buttonResetPassword = findViewById(R.id.bttResetPassword);
        edtNewPassword = findViewById(R.id.edtNewPassword);
        bttdangnhap =findViewById(R.id.bttdangnhap);
        bttdangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ForgotPassword.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        textLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgotPassword.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        buttonResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });
    }

    private void resetPassword() {
        String email = editTextEmail.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter your registered email", Toast.LENGTH_SHORT).show();
            return;
        }

        firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Password reset email sent successfully
                        Toast.makeText(ForgotPassword.this, "Password reset email sent. Check your email.", Toast.LENGTH_SHORT).show();

                        // Get the new password from the EditText
                        String newPassword = edtNewPassword.getText().toString().trim();
                        // Update the password in the Realtime Database
                        updatePasswordInDatabase(email, newPassword);
                    } else {
                        // Handle specific password reset failure cases
                        try {
                            throw task.getException();
                        } catch (Exception e) {
                            // Check if the email is not registered or invalid
                            if (e instanceof FirebaseAuthInvalidUserException) {
                                Toast.makeText(ForgotPassword.this, "Email not registered. Please enter a valid email.", Toast.LENGTH_SHORT).show();
                            } else {
                                // Handle other password reset errors
                                Toast.makeText(ForgotPassword.this, "Failed to reset password: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private void updatePasswordInDatabase(String userEmail, String newPassword) {
        // Query for the specific user with the target email
        Query query = databaseReference.child("User").orderByChild("user_email").equalTo(userEmail);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.hasChildren()) {
                    // Assuming there is only one user with the given email, retrieve the first child
                    DataSnapshot userSnapshot = snapshot.getChildren().iterator().next();

                    // Get the user ID from the snapshot
                    String userId = userSnapshot.getKey();

                    // Update the user's password in the database
                    databaseReference.child("User").child(userId).child("user_password").setValue(newPassword)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        // Password updated in the Realtime Database
                                        Toast.makeText(ForgotPassword.this, "Password updated in the Realtime Database.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        // Handle password update errors
                                        Toast.makeText(ForgotPassword.this, "Failed to update password in the Realtime Database: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    // Handle the case when no user with the given email is found
                    Toast.makeText(ForgotPassword.this, "No user found with the email: " + userEmail, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error if needed
                Toast.makeText(ForgotPassword.this, "Error loading data from Firebase: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
