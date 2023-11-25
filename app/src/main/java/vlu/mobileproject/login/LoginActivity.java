package vlu.mobileproject.login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import vlu.mobileproject.R;
import vlu.mobileproject.translate.LanguageHelper;

// ... (Your imports and other code)

public class LoginActivity extends AppCompatActivity {

    Button btnTranslate;


    private EditText emailEditText; // Updated to the correct EditText for email input
    private EditText passwordEditText;
    private Button loginButton;
    private TextView taotaikhoan,quenmk;
    private ImageView showPasswordIcon;
    private boolean isPasswordVisible = false;

    private DatabaseReference usersRef;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnTranslate = findViewById(R.id.btnTranslate);
        btnTranslate.setOnClickListener(v-> {
            LanguageHelper.changeLanguage(getResources(), "vi");
            recreate();
        });


        // Khởi tạo Firebase Database
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://e-commerce-73482-default-rtdb.asia-southeast1.firebasedatabase.app/");
        usersRef = database.getReference("User");

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
    }
private void addEvent(){
    quenmk =findViewById(R.id.quenmk);
    quenmk.setOnClickListener(view -> {
        Intent intent = new Intent(LoginActivity.this, ForgotPassword.class);
        startActivity(intent);
    });
    taotaikhoan =findViewById(R.id.taotaikhoan);
    taotaikhoan.setOnClickListener(view -> {
        Intent intent = new Intent(LoginActivity.this, sigup.class);
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
            Intent intent = new Intent(LoginActivity.this, sigup.class);
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

        // Use "user_email" instead of "user" for ordering and querying
        Query query = usersRef.orderByChild("user_email").equalTo(email);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    // There should be only one user with the specified email
                    DataSnapshot userSnapshot = dataSnapshot.getChildren().iterator().next();
                    Object storedPasswordObject = userSnapshot.child("user_password").getValue(); // Removed String.class

                    if (storedPasswordObject != null) {
                        // Convert the storedPasswordObject to String if it's not null
                        String storedPassword = String.valueOf(storedPasswordObject);

                        if (storedPassword.equals(password)) {
                            // Xác thực thành công
                            Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this,vlu.mobileproject.activity.view.home.MainActivity.class);
                            // Pass user-specific data if needed
                            intent.putExtra("user_email", email);
                            startActivity(intent);
                            finish();
                        } else {
                            // Sai mật khẩu
                            Toast.makeText(LoginActivity.this, "Mật khẩu không đúng", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Handle the case where the password in the database is null
                        Toast.makeText(LoginActivity.this, "Mật khẩu không tồn tại", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Không tìm thấy tài khoản với email nhập vào trong Database
                    Toast.makeText(LoginActivity.this, "Tài khoản không tồn tại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(LoginActivity.this, "Đã xảy ra lỗi: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    protected void onResume() {
        super.onResume();
    }
}
