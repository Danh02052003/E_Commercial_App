package vlu.mobileproject.login;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException;
import com.google.firebase.auth.FirebaseUser;
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
import vlu.mobileproject.translate.LanguageHelper;
import vlu.mobileproject.activity.view.home.MainActivity;

public class LoginActivity extends AppCompatActivity {

    Button btnTranslate;


    private EditText emailEditText; // Updated to the correct EditText for email input
    private EditText passwordEditText;
    private Button loginButton;
    private TextView taotaikhoan, quenmk;
    private ImageView showPasswordIcon;
    private boolean isPasswordVisible = false;

    String verificationId;

    private FirebaseAuth firebaseAuth;

    CheckBox RememberUser;

    FirebaseAuth mAuthLog;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuthLog = FirebaseAuth.getInstance();
        btnTranslate = findViewById(R.id.btnTranslate);
        firebaseAuth = FirebaseAuth.getInstance();
        btnTranslate.setOnClickListener(v -> {
            LanguageHelper.changeLanguage(getResources(), "vi");
            recreate();
        });

        RememberUser = findViewById(R.id.RememberUser);
        emailEditText = findViewById(R.id.editTextUserName); // Updated to the correct ID for email input
        passwordEditText = findViewById(R.id.editTextPassword);
        loginButton = findViewById(R.id.button);
        showPasswordIcon = findViewById(R.id.showPasswordIcon);
        addEvent();
        // Set click listener to the ImageView
        showPasswordIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePasswordVisibility();
            }
        });

        Paper.init(this);
    }

    private void addEvent() {
        quenmk = findViewById(R.id.quenmk);
        quenmk.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, ForgotPassword.class);
            startActivity(intent);
        });
        taotaikhoan = findViewById(R.id.taotaikhoan);
        taotaikhoan.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
        });

        loginButton.setOnClickListener(v -> loginUser());
    }

    private void togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible;
        if (isPasswordVisible) {
            passwordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            showPasswordIcon.setImageResource(R.drawable.open_eye); // Open eye icon
        } else {
            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            showPasswordIcon.setImageResource(R.drawable.close_eye); // Closed eye icon
        }
        passwordEditText.setSelection(passwordEditText.getText().length());


        quenmk = findViewById(R.id.quenmk);
        quenmk.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, ForgotPassword.class);
            startActivity(intent);
        });
        taotaikhoan = findViewById(R.id.taotaikhoan);
        taotaikhoan.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
        });

        loginButton.setOnClickListener(v -> loginUser());
    }

    private void loginUser() {
        final String email = emailEditText.getText().toString().trim();
        boolean isEmail = email.contains("@");
        final String password = passwordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        UserManager.getInstance().setUserEmail(email);
        if (isEmail) {
            mAuthLog.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {

                        Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                        Paper.book().write("RememberUser", RememberUser.isChecked());

                        Intent intent = new Intent(LoginActivity.this, vlu.mobileproject.activity.view.home.MainActivity.class);
                        // Pass user-specific data if needed
                        intent.putExtra("user_email", email);
                        startActivity(intent);
                        finish();

                    } else {
                        Toast.makeText(LoginActivity.this, "Mật khẩu hoặc Email không đúng.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            SendVerificationCode(email);
        }
    }

    void SendVerificationCode(String phoneNumber) {
        checkForPhoneNumber(phoneNumber, exists -> {
            if (exists) {
                PhoneAuthOptions options =
                        PhoneAuthOptions.newBuilder(firebaseAuth)
                                .setPhoneNumber(phoneNumber)       // Phone number to verify
                                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                                .setActivity(this)                 // (optional) Activity for callback binding
                                .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                                .build();
                PhoneAuthProvider.verifyPhoneNumber(options);

                VerifyDialog.showDialog(LoginActivity.this, new VerifyDialog.OnVerifyListener() {
                    @Override
                    public void onVerify(String code) {
                        PhoneAuthCredential credential = VerifyCode(code);
                        signInWithPhoneAuthCredential(credential);
                    }
                });
            } else {
                Toast.makeText(LoginActivity.this, "Tài khoản không tồn tại", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(LoginActivity.this, "Verification Fail Invalid request" + e.getMessage(), Toast.LENGTH_SHORT).show();

                // Invalid request
            } else if (e instanceof FirebaseTooManyRequestsException) {
                Toast.makeText(LoginActivity.this, "Verification Fail The SMS quota for the project has been exceeded" + e.getMessage(), Toast.LENGTH_SHORT).show();

                // The SMS quota for the project has been exceeded
            } else if (e instanceof FirebaseAuthMissingActivityForRecaptchaException) {
                Toast.makeText(LoginActivity.this, "Verification Fail reCAPTCHA" + e.getMessage(), Toast.LENGTH_SHORT).show();
                // reCAPTCHA verification attempted with null Activity
            }
            Log.d(TAG, "Verification Fail Invalid request" + e.getMessage());
            Toast.makeText(LoginActivity.this, "Verification Fail Invalid request" + e.getMessage(), Toast.LENGTH_SHORT).show();

            // Show a message and update the UI
        }

        @Override
        public void onCodeSent(@NonNull String s,
                               @NonNull PhoneAuthProvider.ForceResendingToken token) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
            Log.d(TAG, "onCodeSent:" + s);
            Toast.makeText(LoginActivity.this, "On code send", Toast.LENGTH_SHORT).show();

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
        UserManager.getInstance().setUserEmail(firebaseAuth.getCurrentUser().getEmail());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Đặng nhập thành công", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
    }

}


