package vlu.mobileproject.login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import io.paperdb.Paper;
import vlu.mobileproject.R;
import vlu.mobileproject.activity.view.home.MainActivity;
import vlu.mobileproject.login.data.LoginParent;

public class LoginActivity extends LoginParent {

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private TextView taotaikhoan, quenmk, loginWithPhone;
    private ImageView showPasswordIcon;
    private boolean isPasswordVisible = false;

    CheckBox RememberUser;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        addControl();
        addEvent();
    }

    private void addControl(){

        RememberUser = findViewById(R.id.RememberUser);
        emailEditText = findViewById(R.id.userPhoneNumber); // Updated to the correct ID for email input
        passwordEditText = findViewById(R.id.editTextPassword);
        loginButton = findViewById(R.id.loginbutton);
        showPasswordIcon = findViewById(R.id.showPasswordIcon);
    }
    private void addEvent() {
        quenmk = findViewById(R.id.quenmk);
        loginWithPhone = findViewById(R.id.loginWithPhone);

        loginWithPhone.setOnClickListener(v -> {
            OpenLoginWithPhoneActivity();
        });
        quenmk.setOnClickListener(view -> {
            OpenForgotPasswordActivity();
        });
        taotaikhoan = findViewById(R.id.taotaikhoan);
        taotaikhoan.setOnClickListener(view -> {
            OpenSignupActivity();
        });

        loginButton.setOnClickListener(v -> loginUser());
        showPasswordIcon.setOnClickListener(v -> togglePasswordVisibility());
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
    }

    @Override
    protected void loginUser() {
        super.loginUser();

        final String email = emailEditText.getText().toString().trim();
        if (!email.contains("@")) {
            Toast.makeText(this, "Vui lòng nhập đúng email", Toast.LENGTH_SHORT).show();
            return;
        }
        final String password = passwordEditText.getText().toString().trim();
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                Paper.book().write("RememberUser", RememberUser.isChecked());

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);

                intent.putExtra("user_email", email);
                startActivity(intent);
                finish();

            } else {
                Toast.makeText(LoginActivity.this, "Mật khẩu hoặc Email không đúng.", Toast.LENGTH_SHORT).show();
            }
        });
    }


}


