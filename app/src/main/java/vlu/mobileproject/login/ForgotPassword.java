package vlu.mobileproject.login;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import vlu.mobileproject.R;
import vlu.mobileproject.activity.view.home.MainActivity;

public class ForgotPassword extends AppCompatActivity {
    private TextView textLogin,bttdangnhap;
    private EditText editTextEmailPhone;
    private Button buttonResetPassword;
    private EditText edtNewPassword;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    String verificationId;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        textLogin = findViewById(R.id.dangNhap);
        editTextEmailPhone = findViewById(R.id.edtEmailPhone);
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
        String emailPhone = editTextEmailPhone.getText().toString().trim();
        boolean isEmail = emailPhone.contains("@");

        if (TextUtils.isEmpty(emailPhone)) {
            Toast.makeText(this, "Please enter your registered email", Toast.LENGTH_SHORT).show();
            return;
        }

        if (isEmail) {
            firebaseAuth.sendPasswordResetEmail(emailPhone)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Password reset email sent successfully
                            Toast.makeText(ForgotPassword.this, "Password reset email sent. Check your email.", Toast.LENGTH_SHORT).show();

                            // Get the new password from the EditText
                            String newPassword = edtNewPassword.getText().toString().trim();
                            // Update the password in the Realtime Database
                            updatePasswordInDatabase(emailPhone, newPassword);
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
        } else {
            SendVerificationCode(emailPhone);

        }
    }

    void SendVerificationCode(String phoneNumber) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // (optional) Activity for callback binding
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

        VerifyDialog.showDialog(ForgotPassword.this, code -> {
            PhoneAuthCredential credential = VerifyCode(code);
            signInWithPhoneAuthCredential(credential);
        });
    }

    void ChangeUserPassword() {
        FirebaseUser user2ChangePass = FirebaseAuth.getInstance().getCurrentUser();
        String newPassword = edtNewPassword.getText().toString().trim();
        user2ChangePass.updatePassword(newPassword)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(ForgotPassword.this, "Đổi mật khẩu thành công.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ForgotPassword.this, "Đổi mật khẩu không thành công.", Toast.LENGTH_SHORT).show();
                        Exception exception = task.getException();
                    }
                });
    }

    void checkForPhoneNumber(String number, Consumer<Boolean> callback) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

        ref.orderByChild("phone").equalTo(number).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                callback.accept(dataSnapshot.exists());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks

            mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.
            //signInWithPhoneAuthCredential(credential);


            final  String code = credential.getSmsCode();
            if (code != null) {
                VerifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                Toast.makeText(ForgotPassword.this, "Verification Fail Invalid request" + e.getMessage(), Toast.LENGTH_SHORT).show();

                // Invalid request
            } else if (e instanceof FirebaseTooManyRequestsException) {
                Toast.makeText(ForgotPassword.this, "Verification Fail The SMS quota for the project has been exceeded" + e.getMessage(), Toast.LENGTH_SHORT).show();

                // The SMS quota for the project has been exceeded
            } else if (e instanceof FirebaseAuthMissingActivityForRecaptchaException) {
                Toast.makeText(ForgotPassword.this, "Verification Fail reCAPTCHA" + e.getMessage(), Toast.LENGTH_SHORT).show();
                // reCAPTCHA verification attempted with null Activity
            }
            Log.d(TAG, "Verification Fail Invalid request" + e.getMessage());
            Toast.makeText(ForgotPassword.this, "Verification Fail Invalid request" + e.getMessage(), Toast.LENGTH_SHORT).show();

            // Show a message and update the UI
        }

        @Override
        public void onCodeSent(@NonNull String s,
                               @NonNull PhoneAuthProvider.ForceResendingToken token) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
            Log.d(TAG, "onCodeSent:" + s);
            Toast.makeText(ForgotPassword.this, "On code send", Toast.LENGTH_SHORT).show();

            // Save verification ID and resending token so we can use them later
            super.onCodeSent(s, token);
            verificationId = s;
        }
    };

    PhoneAuthCredential VerifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        return credential;
    }

    void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            ChangeUserPassword();
                            UserManager.getInstance().setUserEmail(firebaseAuth.getCurrentUser().getEmail());

                            Intent intent = new Intent(ForgotPassword.this, MainActivity.class);
                            startActivity(intent);
                            finish();
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
