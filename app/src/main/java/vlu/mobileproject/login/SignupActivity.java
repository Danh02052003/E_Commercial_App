package vlu.mobileproject.login;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

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
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException;
import com.google.firebase.auth.FirebaseAuthSettings;
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

    String verificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sigup);

        firebaseAuth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference("User");
        editPhone =findViewById(R.id.phoneNumber);
        editTextUsername = findViewById(R.id.userName);
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.passWord);
        buttonSignup = findViewById(R.id.bttSignup);
        dangnhap = findViewById(R.id.dangNhap);
        transaction = getSupportFragmentManager().beginTransaction();
        dangnhap.setOnClickListener(view -> {
            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
            startActivity(intent);
        });
        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signupUser();
            }
        });
    }
    private boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
    private void signupUser() {
        final String username = editTextUsername.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String phone = editPhone.getText().toString().trim();

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
        if (phone.length() != 10) {
            //Toast.makeText(this, "\n" + "Số điện thoại phải có chính xác 10 chữ số", Toast.LENGTH_SHORT).show();
            //return;
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
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                            if (currentUser != null) {
                                // User registration successful

                                SendVerificationCode(phone);
                                VerifyDialog.showDialog(SignupActivity.this, new VerifyDialog.OnVerifyListener() {
                                    @Override
                                    public void onVerify(String code) {
                                        PhoneAuthCredential credential = VerifyCode(code);

                                        saveUserDataToDatabase(username , credential);
                                        signInWithPhoneAuthCredential(credential);
                                    }
                                });
                            }
                        } else {
                            // Handle specific registration failure cases
                            try {
                                throw task.getException();
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

                VerifyDialog.showDialog(SignupActivity.this, new VerifyDialog.OnVerifyListener() {
                    @Override
                    public void onVerify(String code) {
                        PhoneAuthCredential credential = VerifyCode(code);
                        signInWithPhoneAuthCredential(credential);
                    }
                });
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
                            Toast.makeText(SignupActivity.this, "Đặng nhập thành công", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
    }

    private void saveUserDataToDatabase(String user_name, PhoneAuthCredential credential) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        user newUser = new user(user_name);
        usersRef.child(userId).setValue(newUser)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        firebaseAuth.getCurrentUser().updatePhoneNumber(credential);
                        Paper.init(this);
                        Paper.book().write("RememberUser", true);
                        Toast.makeText(SignupActivity.this, "Đặng kí thành công", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(SignupActivity.this, "\n" +
                                "Không thể lưu thông tin người dùng", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void sendVerificationEmail() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {

            user.sendEmailVerification()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {

                            Toast.makeText(SignupActivity.this, "Email xác nhận đã được gửi", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();

                        } else {

                            Toast.makeText(SignupActivity.this, "Không thể gửi email xác minh\n", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(SignupActivity.this, "Không tìm thấy người dùng. Vui lòng thử lại", Toast.LENGTH_SHORT).show();
        }
    }

    private String generateVerificationCode() {
        int min = 100000;
        int max = 999999;
        int verificationCodeInt = min + (int) (Math.random() * (max - min + 1));
        return String.valueOf(verificationCodeInt);
    }
}
