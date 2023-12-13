package vlu.mobileproject.login.data;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import io.paperdb.Paper;
import vlu.mobileproject.R;
import vlu.mobileproject.activity.view.home.MainActivity;
import vlu.mobileproject.login.ForgotPassword;
import vlu.mobileproject.login.LoginActivity;
import vlu.mobileproject.login.PhoneLoginActivity;
import vlu.mobileproject.login.SignupActivity;
import vlu.mobileproject.login.VerifyDialog;
import vlu.mobileproject.login.data.model.IOnSuccLogin;

public class LoginParent extends AppCompatActivity implements IOnSuccLogin {

    protected FirebaseAuth firebaseAuth;
    protected String verificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_login);
        Paper.init(this);
        firebaseAuth = FirebaseAuth.getInstance();

    }

    protected void loginUser() {

    }

    protected void OpenForgotPasswordActivity() {
        Intent intent = new Intent(LoginParent.this, ForgotPassword.class);
        startActivity(intent);
    }

    protected void OpenLoginWithEmailActivity() {
        Intent intent = new Intent(LoginParent.this, LoginActivity.class);
        startActivity(intent);
    }

    protected void OpenLoginWithPhoneActivity() {
        Intent intent = new Intent(LoginParent.this, PhoneLoginActivity.class);
        startActivity(intent);
    }

    protected void OpenSignupActivity() {
        Intent intent = new Intent(LoginParent.this, SignupActivity.class);
        startActivity(intent);
    }

    protected void SendVerificationCode(String phoneNumber) {
        checkForPhoneNumber(phoneNumber, exists -> {
            if (!exists) {
                PhoneAuthOptions options =
                        PhoneAuthOptions.newBuilder(firebaseAuth)
                                .setPhoneNumber(phoneNumber)       // Phone number to verify
                                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                                .setActivity(this)                 // (optional) Activity for callback binding
                                .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                                .build();
                PhoneAuthProvider.verifyPhoneNumber(options);

                VerifyDialog.showDialog(phoneNumber, LoginParent.this, code -> {
                    PhoneAuthCredential credential = VerifyCode(code);
                    signInWithPhoneAuthCredential(credential);
                });
            } else {
                Toast.makeText(LoginParent.this, "Tài khoản không tồn tại", Toast.LENGTH_SHORT).show();
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

    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks

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
                Toast.makeText(LoginParent.this, "Verification Fail Invalid request" + e.getMessage(), Toast.LENGTH_SHORT).show();

                // Invalid request
            } else if (e instanceof FirebaseTooManyRequestsException) {
                Toast.makeText(LoginParent.this, "Verification Fail The SMS quota for the project has been exceeded" + e.getMessage(), Toast.LENGTH_SHORT).show();

                // The SMS quota for the project has been exceeded
            } else if (e instanceof FirebaseAuthMissingActivityForRecaptchaException) {
                Toast.makeText(LoginParent.this, "Verification Fail reCAPTCHA" + e.getMessage(), Toast.LENGTH_SHORT).show();
                // reCAPTCHA verification attempted with null Activity
            }
            Log.d(TAG, "Verification Fail Invalid request" + e.getMessage());
            Toast.makeText(LoginParent.this, "Verification Fail Invalid request" + e.getMessage(), Toast.LENGTH_SHORT).show();

            // Show a message and update the UI
        }

        @Override
        public void onCodeSent(@NonNull String s,
                               @NonNull PhoneAuthProvider.ForceResendingToken token) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
            Log.d(TAG, "onCodeSent:" + s);
            Toast.makeText(LoginParent.this, "On code send", Toast.LENGTH_SHORT).show();

            // Save verification ID and resending token so we can use them later
            super.onCodeSent(s, token);
            verificationId = s;
        }
    };

    PhoneAuthCredential VerifyCode(String code) {
        return PhoneAuthProvider.getCredential(verificationId, code);
    }

    protected void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        try {
            firebaseAuth.signInWithCredential(credential)
                    .addOnFailureListener(command -> {
                        Toast.makeText(LoginParent.this, "CAN'T signInWithCredential " + command, Toast.LENGTH_SHORT).show();
                    })
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            OnLoginSuccessful();

                            Intent intent = new Intent(LoginParent.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });

        } catch (Exception e) {
            Toast.makeText(LoginParent.this, "Đặng nhập thất bại " + e, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void OnLoginSuccessful() {
        Toast.makeText(LoginParent.this, "Đặng nhập thành công", Toast.LENGTH_SHORT).show();
    }
}
