package vlu.mobileproject.login;

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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import vlu.mobileproject.R;

public class sigup extends AppCompatActivity {
    private EditText editTextUsername;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editPhone;
    private Button buttonSignup;
    private TextView dangnhap;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference usersRef;

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
        dangnhap.setOnClickListener(view -> {
            Intent intent = new Intent(sigup.this, LoginActivity.class);
            startActivity(intent);
        });
        buttonSignup.setOnClickListener(v -> signupUser());
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
            Toast.makeText(this, "\n" +
                    "Số điện thoại phải có chính xác 10 chữ số", Toast.LENGTH_SHORT).show();
            return;
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
                                String verificationCode = generateVerificationCode();
                                saveUserDataToDatabase(username, email, password,phone, verificationCode);
                            }
                        } else {
                            // Handle specific registration failure cases
                            try {
                                throw task.getException();
                            } catch (Exception e) {
                                // Check if the email is already in use by another user
                                if (e instanceof FirebaseAuthUserCollisionException) {
                                    Toast.makeText(sigup.this, "\n" +
                                            "Email đã được sử dụng. Vui lòng chọn một email khác.", Toast.LENGTH_SHORT).show();
                                } else {
                                    // Handle other registration errors
                                    Toast.makeText(sigup.this, "\n" +
                                            "Đăng ky thât bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                });
    }

    private void saveUserDataToDatabase(String user_name, String user_email, String user_password, String user_phone, String verificationCode) {

        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("User");


        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();


        user newUser = new user(user_name, user_email, user_password, user_phone, verificationCode);


        usersRef.child(userId).setValue(newUser)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        sendVerificationEmail();
                    } else {

                        Toast.makeText(sigup.this, "\n" +
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

                            Toast.makeText(sigup.this, "Email xác nhận đã được gửi", Toast.LENGTH_SHORT).show();

                        } else {

                            Toast.makeText(sigup.this, "Không thể gửi email xác minh\n", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {

            Toast.makeText(sigup.this, "Không tìm thấy người dùng. Vui lòng thử lại", Toast.LENGTH_SHORT).show();
        }
    }

    private String generateVerificationCode() {
        int min = 100000;
        int max = 999999;
        int verificationCodeInt = min + (int) (Math.random() * (max - min + 1));
        return String.valueOf(verificationCodeInt);
    }
}
