package vlu.mobileproject.login;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import io.paperdb.Paper;
import vlu.mobileproject.R;
import vlu.mobileproject.activity.view.home.MainActivity;

public class SignupActivity extends AppCompatActivity {
    private EditText editTextUsername;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editPhone;
    private Button buttonSignup;
    private TextView dangnhap;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference usersRef;

    FragmentTransaction transaction;

    String username;

    String verificationId;
    String phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sigup);
        addControl();

        transaction = getSupportFragmentManager().beginTransaction();
        dangnhap.setOnClickListener(view -> {
            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
            startActivity(intent);
        });
        buttonSignup.setOnClickListener(v -> signupUser());
    }
    private void addControl(){
        firebaseAuth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference("User");
        editPhone =findViewById(R.id.phoneNumber);
        editTextUsername = findViewById(R.id.userName);
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.passWord);
        buttonSignup = findViewById(R.id.bttSignup);
        dangnhap = findViewById(R.id.dangNhap);
    }
    private boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
    private void signupUser() {
        username = editTextUsername.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        phone = editPhone.getText().toString().trim();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "\n" +
                    "Vui lòng nhập tất cả các thông tin ", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length() < 6) {
            Toast.makeText(this, "\n" +
                    "Mật khẩu phải có độ dài ít nhất 6 ký tự", Toast.LENGTH_SHORT).show();
            return;
        }
        if (phone.length() != 10) { // Kệ chỗ này. SDT nước ngoài khác vn, đéo test số ảo được lol.
            //Toast.makeText(this, "\n" + "Số điện thoại phải có chính xác 10 chữ số", Toast.LENGTH_SHORT).show();
        }
        if (!isValidEmail(email)) {
            Toast.makeText(this, "\n" +
                    "Địa chỉ email không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        String specialCharacters = "[!@#$%^&*(),.?\":{}|<>]";
        if (password.matches(".*" + specialCharacters + ".*")) {
            Toast.makeText(this, "Mật khẩu không được chứa ký tự đặc biệt", Toast.LENGTH_SHORT).show();
            return;
        }

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                        if (currentUser != null) {
                            SendVerificationCode(phone);
                            VerifyDialog.showDialog(SignupActivity.this, code -> {
                                PhoneAuthCredential credential = VerifyCode(code);
                                saveUserDataToDatabase(username, credential);
                            });
                        }
                    } else {
                        // Handle specific registration failure cases
                        try {
                            throw Objects.requireNonNull(task.getException());
                        } catch (Exception e) {
                            // Check if the email is already in use by another user
                            if (e instanceof FirebaseAuthUserCollisionException) {
                                Toast.makeText(SignupActivity.this, "\n" +
                                        "Email đã được sử dụng. Vui lòng chọn một email khác.", Toast.LENGTH_SHORT).show();
                            } else {
                                // Handle other registration errors
                                Toast.makeText(SignupActivity.this, "\n" +
                                        "Đăng ky thât bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    void SendVerificationCode(String phoneNumber) {
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
            } else {
                Toast.makeText(SignupActivity.this, "Số này đã được sử dụng", Toast.LENGTH_SHORT).show();
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
            saveUserDataToDatabase(username, credential);
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                Toast.makeText(SignupActivity.this, "Verification Fail Invalid request" + e.getMessage(), Toast.LENGTH_SHORT).show();

                // Invalid request
            } else if (e instanceof FirebaseTooManyRequestsException) {
                Toast.makeText(SignupActivity.this, "Verification Fail The SMS quota for the project has been exceeded" + e.getMessage(), Toast.LENGTH_SHORT).show();

                // The SMS quota for the project has been exceeded
            } else if (e instanceof FirebaseAuthMissingActivityForRecaptchaException) {
                Toast.makeText(SignupActivity.this, "Verification Fail reCAPTCHA" + e.getMessage(), Toast.LENGTH_SHORT).show();
                // reCAPTCHA verification attempted with null Activity
            }
            Log.d(TAG, "Verification Fail Invalid request" + e.getMessage());
            Toast.makeText(SignupActivity.this, "Verification Fail Invalid request" + e.getMessage(), Toast.LENGTH_SHORT).show();

            // Show a message and update the UI
        }

        @Override
        public void onCodeSent(@NonNull String s,
                               @NonNull PhoneAuthProvider.ForceResendingToken token) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
            Log.d(TAG, "onCodeSent:" + s);
            Toast.makeText(SignupActivity.this, "On code send", Toast.LENGTH_SHORT).show();

            // Save verification ID and resending token so we can use them later
            super.onCodeSent(s, token);
            verificationId = s;
        }
    };

    PhoneAuthCredential VerifyCode(String code) {
        return PhoneAuthProvider.getCredential(verificationId, code);
    }

    void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        UserManager.getInstance().setUserEmail(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getEmail());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(SignupActivity.this, "Đặng nhập thành công", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
    }

    private void saveUserDataToDatabase(String user_name, PhoneAuthCredential credential) {
        String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        user newUser = new user(user_name);

        // Check if the phone number is not already associated with another account
        checkForPhoneNumber(phone, exists -> {
            if (!exists) {
                // Update the phone number and save user data to the database
                Objects.requireNonNull(firebaseAuth.getCurrentUser()).updatePhoneNumber(credential)
                        .addOnCompleteListener(phoneUpdateTask -> {
                            if (phoneUpdateTask.isSuccessful()) {
                                usersRef.child(userId).setValue(newUser)
                                        .addOnCompleteListener(task -> {
                                            if (task.isSuccessful()) {
                                                Paper.init(this);
                                                Paper.book().write("RememberUser", true);
                                                Toast.makeText(SignupActivity.this, "Đặng kí thành công", Toast.LENGTH_SHORT).show();
                                                signInWithPhoneAuthCredential(credential);
                                            } else {
                                                firebaseAuth.getCurrentUser().delete();
                                                // Handle database save failure
                                                Toast.makeText(SignupActivity.this, "\n" +
                                                        "Không thể lưu thông tin người dùng: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            } else {
                                firebaseAuth.getCurrentUser().delete();
                                // Handle phone number update failure
                                Toast.makeText(SignupActivity.this, "Failed to update phone number: " + Objects.requireNonNull(phoneUpdateTask.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                // The phone number is already associated with another account
                Toast.makeText(SignupActivity.this, "Số điện thoại này đã được sử dụng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String generateVerificationCode() {
        int min = 100000;
        int max = 999999;
        int verificationCodeInt = min + (int) (Math.random() * (max - min + 1));
        return String.valueOf(verificationCodeInt);
    }
}
