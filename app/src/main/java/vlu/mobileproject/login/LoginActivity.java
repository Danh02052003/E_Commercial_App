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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import io.paperdb.Paper;
import vlu.mobileproject.R;
import vlu.mobileproject.translate.LanguageHelper;
import vlu.mobileproject.activity.view.home.MainActivity;

public class LoginActivity extends AppCompatActivity {

    Button btnTranslate;


    private EditText emailEditText; // Updated to the correct EditText for email input
    private EditText passwordEditText;
    private Button loginButton;
    private TextView taotaikhoan,quenmk;
    private ImageView showPasswordIcon;
    private boolean isPasswordVisible = false;

    CheckBox RememberUser;

    FirebaseAuth mAuthLog;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuthLog = FirebaseAuth.getInstance();
        btnTranslate = findViewById(R.id.btnTranslate);
        btnTranslate.setOnClickListener(v-> {
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
private void addEvent(){
    quenmk = findViewById(R.id.quenmk);
    quenmk.setOnClickListener(view -> {
        Intent intent = new Intent(LoginActivity.this, ForgotPassword.class);
        startActivity(intent);
    });
    taotaikhoan =findViewById(R.id.taotaikhoan);
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


        quenmk =findViewById(R.id.quenmk);
        quenmk.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, ForgotPassword.class);
            startActivity(intent);
        });
        taotaikhoan =findViewById(R.id.taotaikhoan);
        taotaikhoan.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
        });

        loginButton.setOnClickListener(v -> loginUser());
    }

    private void loginUser() {
        final String email = emailEditText.getText().toString().trim();
        UserManager.getInstance().setUserEmail(email);
        final String password = passwordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuthLog.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                            if (RememberUser.isChecked()) {
                                Paper.book().write("UserEmailKey", email);
                                Paper.book().write("UserPassKey", password);
                            }

                            Intent intent = new Intent(LoginActivity.this,vlu.mobileproject.activity.view.home.MainActivity.class);
                            // Pass user-specific data if needed
                            intent.putExtra("user_email", email);
                            startActivity(intent);
                            finish();

                        } else {
                            Toast.makeText(LoginActivity.this, "Mật khẩu hoặc Email không đúng.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }}